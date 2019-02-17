package com.example.tfc.factory.writer;

import com.example.tfc.factory.commons.Constants;
import com.example.tfc.factory.commons.dto.PanelDTO;
import com.example.tfc.factory.commons.dto.TypeScriptFunctionDTO;
import com.example.tfc.factory.commons.dto.TypeScriptImportDTO;
import com.example.tfc.factory.utils.TypeScriptTemplateUtils;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Component
@Qualifier("serviceWriter")
public class ServiceWriter implements Writer {

    @Override
    public void write(PanelDTO panelDTO) {
        Path path = Paths.get( Constants.FULL_SERVICE_FOLDER_PATH + Constants.GLOBAL_SERVICE_FILE_NAME);

        try {

            StringBuilder builder = new StringBuilder();

            setDefaultImport(panelDTO);

            panelDTO.getService().getImports().forEach(i -> builder.append(TypeScriptTemplateUtils.getImport(i)));

            builder.append("\n");
            builder.append(TypeScriptTemplateUtils.getInjectableDeclaration());

            StringBuilder body = new StringBuilder();

            body.append(TypeScriptTemplateUtils.getConstructor("HttpClient"));
            body.append("\n");

            panelDTO.getService().getDescriptions().forEach(d -> {
                String name = getServiceFunctionName(d.getName());
                String[] param = new String[] {"queryString"};
                String rtrn = "this.httpClient." + d.getMethod() + "(`" + d.getUrl() + "${queryString}`)";
                body.append(TypeScriptTemplateUtils.getFunction(new TypeScriptFunctionDTO("public", name, param, "", rtrn)));
            });

            builder.append(TypeScriptTemplateUtils.getClassDeclaration( Constants.GLOBAL_SERVICE_NAME, body.toString(), false));
            Files.write(path, builder.toString().getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static String getServiceFunctionName(String serviceName){
        return "service" + serviceName;
    }

    private void setDefaultImport(PanelDTO panelDTO) {
        String[] injectable = {"Injectable"};
        panelDTO.getService().getImports().add(new TypeScriptImportDTO(injectable, "@angular/core"));
        String[] httpClient = {"HttpClient"};
        panelDTO.getService().getImports().add(new TypeScriptImportDTO(httpClient, "@angular/common/http"));
    }
}

