package com.Vcard.EmployeeService.Exception;

import feign.Response;
import feign.codec.ErrorDecoder;

//public class CustomFeignErrorDecoder implements ErrorDecoder {
//	private final ErrorDecoder defaultErrorDecoder = new Default();
//
//	@Override
//	public Exception decode(String methodKey, Response response) {
//		if (response.status() == 404) {
//            return new CustomRunTimeException("Resource not found");
//        }
//        // You can handle other status codes as needed
//        return defaultErrorDecoder.decode(methodKey, response);
//	}
//
//    
//}
