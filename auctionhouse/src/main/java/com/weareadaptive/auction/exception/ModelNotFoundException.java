package com.weareadaptive.auction.exception;

public class ModelNotFoundException extends RuntimeException {
    public ModelNotFoundException(){
        super("Item you are looking for not found");
    }
}
