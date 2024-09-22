package com.ecobazzar.exception;

public class FileExistsException extends RuntimeException{
	public FileExistsException(String msg){
		super(msg);
	}
}
