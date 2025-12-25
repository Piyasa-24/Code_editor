package com.editor.code.controllers;

import java.io.IOException;
import java.nio.file.Path;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.editor.code.Model.CodeTemplate;
import com.editor.code.Service.CodeService;




@RestController
@RequestMapping("/code")

public class MainController {

@Autowired
private CodeService codeService;

@PostMapping("/add")

    public String save (@RequestBody CodeTemplate obj) throws IOException
    {
    
        return codeService.saveCode(obj.getCodeBody(),obj.getLangType(),obj.getFileName()).toString();
    }

@PostMapping("/execute")
    public ResponseEntity<String> saveAndExecute(@RequestBody CodeTemplate codeTemplate){
        try{
                Path filePath  = codeService.saveCode(codeTemplate.getCodeBody(), codeTemplate.getLangType(), codeTemplate.getFileName());
                String output = codeService.codeRun(filePath);
                return ResponseEntity.ok(output);
        }
        catch(Exception ex){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body("Error: "+ex.getMessage());
        }
    }

}
