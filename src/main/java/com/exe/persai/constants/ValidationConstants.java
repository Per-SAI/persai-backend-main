package com.exe.persai.constants;

import lombok.experimental.UtilityClass;

@UtilityClass
public class ValidationConstants {
    public final String EMAIL_REGEX = "^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$";
    public final String EMAIL_FORMAT_MESSAGE = "Invalid email format";
    public final String NAME_REGEX = "^[a-zA-ZÀÁÂÃÈÉÊÌÍÒÓÔÕÙÚĂĐĨŨƠàáâãèéêìíòóôõùúăđĩũơƯĂẠẢẤẦẨẪẬẮẰẲẴẶẸẺẼỀỀỂẾưăạảấầẩẫậắằẳẵặẹẻẽềềểếỄỆỈỊỌỎỐỒỔỖỘỚỜỞỠỢỤỦỨỪễệỉịọỏốồổỗộớờởỡợụủứừỬỮỰỲỴÝỶỸửữựỳỵỷỹ0-9\s_()-@$]+$";
    public final String NAME_FORMAT_MESSAGE = "Invalid name format";
    public final String NAME_LENGTH_MESSAGE = "Name should contains at least 8 and at most 50 characters";
    public final String STUDY_TIME_MIN_MESSAGE = "Study time must be at least 5 minutes";
    public final String SHORT_BREAK_MIN_MESSAGE = "Short break must be at least 1 minute";
    public final String LONG_BREAK_MIN_MESSAGE = "Long break must be at least 5 minute";
    public final String LONG_BREAK_INTERVAL_MIN_MESSAGE = "Long break interval must be at least 2";
    public final String PAID_TYPE_REGEX = "^(MONTHLY|YEARLY)$";
    public final String PAID_TYPE_MESSAGE = "PaidType must be MONTHLY or YEARLY";

}
