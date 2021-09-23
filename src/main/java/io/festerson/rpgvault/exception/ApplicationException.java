package io.festerson.rpgvault.exception;

import lombok.Getter;

@Getter
public class ApplicationException extends RuntimeException {
    public ApplicationException(String msg, RuntimeException embedded){
        super(msg);
        this.addSuppressed(embedded);
    }

    public ApplicationException(String msg){
        super(msg);
    }
}
