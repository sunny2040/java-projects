package com.surge.vms.exception;


public class VendorNotFoundException extends RuntimeException {

    public VendorNotFoundException(Long id) {
        super("Vendor Id not found : " + id);
    }

}