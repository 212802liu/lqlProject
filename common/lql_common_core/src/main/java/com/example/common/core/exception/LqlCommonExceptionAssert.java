package com.example.common.core.exception;

import com.example.common.core.exception.commonHandler.Assert;
import com.example.common.core.exception.commonHandler.IResponseEnum;

import java.text.MessageFormat;

public interface LqlCommonExceptionAssert extends Assert, IResponseEnum {

    @Override
    default RuntimeException newException(Object... args) {
        /**
         * String messageTemplate = "Hello, {0}. You have {1} new messages.";
         * Object[] args = {"Alice", 5};
         * String formattedMessage = MessageFormat.format(messageTemplate, args);
         * Hello, Alice. You have 5 new messages.
         */
        String msg = MessageFormat.format(this.getMessage(), args);

        return new LqlCommonException(this);
    }

    @Override
    default RuntimeException newException(Throwable t, Object... args) {
        return null;
    }
}
