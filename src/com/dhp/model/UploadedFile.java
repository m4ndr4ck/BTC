package com.dhp.model;

import org.springframework.web.multipart.MultipartFile;

public class UploadedFile
{
    private String type;
    private String name;

    private MultipartFile file;

    public String getType()
    {
        return type;
    }

    public void setType(String type)
    {
        this.type = type;
    }

    public void setFile(MultipartFile file)
    {
        this.file = file;
    }

    public MultipartFile getFile()
    {
        return file;
    }

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}