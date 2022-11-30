package io.festoso.rpgvault.exception;

import lombok.Getter;

@Getter
public class RpgMgrException extends Throwable {

    private Throwable composed;

    public RpgMgrException(){}

    public RpgMgrException(String s){
        super(s);
    }

    public RpgMgrException(Throwable t){
        super(t.getMessage());
        this.composed = t;
    }
}
