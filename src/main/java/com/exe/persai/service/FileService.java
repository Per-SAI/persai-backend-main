package com.exe.persai.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.PutObjectResult;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.amazonaws.util.IOUtils;
import com.exe.persai.constants.MessageConstants;
import com.exe.persai.model.enums.Decision;
import com.exe.persai.model.enums.ExcelMode;
import com.exe.persai.model.exception.BadRequestException;
import com.exe.persai.model.request.CreateStudySetRequest;
import com.exe.persai.repository.ImageRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.imaging.ImageFormats;
import org.apache.commons.imaging.ImageReadException;
import org.apache.commons.imaging.Imaging;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.tika.Tika;
import org.imgscalr.Scalr;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import javax.imageio.stream.ImageInputStream;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class FileService {
    @Value("${cloud.aws.s3.bucket.name}")
    private String s3BucketName;
    @Value("${spring.image.size}")
    private Integer imageSize;
    private final String HASH_ALGORITHM = "MD5";
    private final AmazonS3 amazonS3;
    private final ImageRepository imageRepository;
    public String uploadImage(MultipartFile multipartFile) {
        checkImageFile(multipartFile);
        String fileName;
        try {
            fileName = hashFile(HASH_ALGORITHM, multipartFile.getBytes());
        } catch (IOException e) {
            log.error("Error getting byte of file");
            throw new IllegalStateException(e.getMessage());
        }
        //return when image already exists in S3 bucket
        if (imageRepository.existsByS3ImageName(fileName)) return fileName;
        File file = convertMultipartFiletoFile(multipartFile);
        File resizedFile = resizeImage(file, imageSize);
        PutObjectResult result = amazonS3.putObject(new PutObjectRequest(s3BucketName, fileName, resizedFile));
        file.delete();
        resizedFile.delete();
        return fileName;
    }

    public byte[] downloadFile(String fileName) {
        S3Object s3Object;
        try {
            s3Object = amazonS3.getObject(s3BucketName, fileName);
        } catch (Exception e) {
            log.error("Error getting s3 object", e);
            throw new BadRequestException("File name does not exist in S3");
        }
        S3ObjectInputStream inputStream = s3Object.getObjectContent();
        try {
            byte[] content = IOUtils.toByteArray(inputStream);
            return content;
        } catch (IOException e) {
            log.error("Error converting input stream to byte[]", e);
            throw new IllegalStateException(e.getMessage());
        }
    }

//    private void checkImageFile(MultipartFile multipartFile) {
//        try {
//            InputStream input = multipartFile.getInputStream();
//            BufferedImage ioRead = ImageIO.read(input);
//            ioRead.toString();
//            // It's an image (only BMP, GIF, JPEG, PNG, TIFF and WBMP are recognized).
//        } catch (Exception e) {
//            // It's not an image.
//            System.out.println(e.toString());
//            throw new BadRequestException("Not an image file");
//        }
//    }

    private void checkImageFile(MultipartFile multipartFile) {
        Tika tika = new Tika();
        try (InputStream input = multipartFile.getInputStream()) {
            String mimeType = tika.detect(input);
            if (mimeType.startsWith("image/")) {
                // It's an image.
                return;
            } else {
                throw new BadRequestException("Not an image file");
            }
        } catch (Exception e) {
            throw new BadRequestException(e.getMessage());
        }
    }

    private String hashFile(String algorithm, byte[] data) {
        try {
            byte[] hash = MessageDigest.getInstance(algorithm).digest(data);
            String checksum = new BigInteger(1, hash).toString(16);
            return checksum;
        } catch (NoSuchAlgorithmException e) {
            log.error("Error hashing file data");
            //throw exception
        }
        return null;
    }

    private File convertMultipartFiletoFile(MultipartFile multipartFile) {
        File file = new File(Objects.requireNonNull(multipartFile.getOriginalFilename()));
        try (FileOutputStream fos = new FileOutputStream(file)) {
            fos.write(multipartFile.getBytes());
        }
        catch (IOException e) {
            log.error("Error converting multipart file to file", e);
            throw new IllegalStateException(e.getMessage());
        }
        return file;
    }

    private File resizeImage(File sourceFile, Integer imageSize) {
        try {
            BufferedImage bufferedImage = ImageIO.read(sourceFile);
            BufferedImage outputImage = Scalr.resize(bufferedImage, imageSize);
            File outputFile = new File(sourceFile.getName());
            ImageIO.write(outputImage, "jpg", outputFile);
            return outputFile;
        }
        catch (Exception e) {
            log.error("Error resizing image: ", e);
            throw new IllegalStateException("Error resizing image. May be the file is not in correct image format");
        }
    }

//    private File resizeImage(File sourceFile, Integer imageSize) {
//        try {
//            BufferedImage bufferedImage = Imaging.getBufferedImage(sourceFile);
//            BufferedImage outputImage = Scalr.resize(bufferedImage, imageSize);
//            File outputFile = new File(sourceFile.getName());
//            Imaging.writeImage(outputImage, outputFile, ImageFormats.UNKNOWN);
//            return outputFile;
//        }
//        catch (Exception e) {
//            log.error("Error resizing image: " + e);
//            throw new IllegalStateException("Error resizing image, please ensure the image is in correct format");
//        }
//    }

    public void checkExcelFile(MultipartFile file) {
        if (file.getOriginalFilename() != null &&
                (file.getOriginalFilename().endsWith(".xlsx") ||
                        Objects.equals(file.getContentType(), MessageConstants.EXCEL_CONTENT_TYPE)))
            return;
        throw new BadRequestException("Not an excel file");
    }

    public List<CreateStudySetRequest.CreateQuestionRequest> processExcelFile(MultipartFile multipartExcelFile, ExcelMode excelMode) {
        checkExcelFile(multipartExcelFile);
        try {
            List<CreateStudySetRequest.CreateQuestionRequest> createQuestionRequests = new ArrayList<>();
            InputStream inputStream = multipartExcelFile.getInputStream();
            XSSFWorkbook workbook = new XSSFWorkbook(inputStream);
            XSSFSheet sheet = workbook.getSheet(MessageConstants.EXCEL_SHEET_NAME);
            int rowIndex = 0;
            for (Row row : sheet) {
                if (rowIndex == 0) {
                    rowIndex++;
                    continue;
                }
                int rowNum = row.getRowNum() + 1;
                String question = row.getCell(0) == null ? null : readExcelCell(row.getCell(0));
                boolean isGptGenerated = readExcelCell(row.getCell(1)).equalsIgnoreCase(Decision.YES.toString());
                String correctAnswer = row.getCell(2) != null ? readExcelCell(row.getCell(2)) : null;
                List<String> answersList = new ArrayList<>();
                for (int i = 3; i < row.getLastCellNum(); i++) {
                    answersList.add(readExcelCell(row.getCell(i)));
                }
                if (excelMode.equals(ExcelMode.CREATE)) {
                    if (question == null)
                        throw new BadRequestException("Question can not be blank at row: " + rowNum);
                    if (answersList.stream().anyMatch(answer -> !StringUtils.hasText(answer)))
                        throw new BadRequestException("Each question can not contain blank answer at row: " + rowNum);
                    if (StringUtils.hasText(correctAnswer)) {
                        if (answersList.stream().noneMatch(answer -> answer.equalsIgnoreCase(correctAnswer)))
                            throw new BadRequestException("Correct answer " + correctAnswer
                                    + " is not included in the list of answers " + answersList + " at row: " + rowNum);
                        if (isGptGenerated)
                            throw new BadRequestException("Correct answer has been inputted. GPT only generates answer for non-input correct answer. " +
                                    "Leave the column isGPTGenerated with NO value at row: " + rowNum);
                    }
                    else if (!isGptGenerated)
                        throw new BadRequestException("Please choose to input correct answer or let GPT generates at row: " + rowNum);
                }

                createQuestionRequests.add(new CreateStudySetRequest.CreateQuestionRequest(
                        question,
                        answersList.toArray(new String[0]),
                        correctAnswer,
                        isGptGenerated
                ));
            }
            return createQuestionRequests;
        }
        catch (BadRequestException e) {
            throw e;
        }
        catch (Exception e) {
            log.error("Error processing excel file: " + e);
            throw new BadRequestException("Can not process excel file. May be the file is incorrect format. Make sure not to leave any cells blank.");
        }
    }

    private String readExcelCell(Cell cell) {
        if (cell.getCellType().equals(CellType.STRING))
            return cell.getStringCellValue();
        if (cell.getCellType().equals(CellType.NUMERIC))
            return String.valueOf(cell.getNumericCellValue());
        if (cell.getCellType().equals(CellType.BOOLEAN))
            return String.valueOf(cell.getBooleanCellValue());
        return null;
    }


}
