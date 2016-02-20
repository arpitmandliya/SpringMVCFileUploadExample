package org.arpit.java2blog.controller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.arpit.java2blog.model.FileUpload;
import org.arpit.java2blog.validator.FileUploadValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class FileUploadController {

	@Autowired
	FileUploadValidator fileValidator;

	@RequestMapping("/fileUploadForm")
	public ModelAndView getUploadForm(
			@ModelAttribute("uploadedFile") FileUpload uploadedFile,
			BindingResult result) {
		return new ModelAndView("fileUploadForm");
	}


	// When click on submit, below method will get called
	@RequestMapping("/submitFileUpload")
	public ModelAndView fileUploaded(
			@ModelAttribute("uploadedFile") FileUpload uploadedFile,
			BindingResult result) {
		InputStream inputStream = null;
		OutputStream outputStream = null;

		// Getting uploaded file
		MultipartFile file = uploadedFile.getFile();
		fileValidator.validate(uploadedFile, result);

		String fileName = file.getOriginalFilename();

		// If it has error, redirect it to same page
		if (result.hasErrors()) {
			return new ModelAndView("fileUploadForm");
		}

		try {
			inputStream = file.getInputStream();

			File newFile = new File("/Users/Arpit/Documents/SpringMVCUploadedFiles/" + fileName);
			if (!newFile.exists()) {
				newFile.createNewFile();
			}
			outputStream = new FileOutputStream(newFile);
			int read = 0;
			byte[] bytes = new byte[1024];

			while ((read = inputStream.read(bytes)) != -1) {
				outputStream.write(bytes, 0, read);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return new ModelAndView("fileUploadSuccess", "fileName", fileName);
	}

}

