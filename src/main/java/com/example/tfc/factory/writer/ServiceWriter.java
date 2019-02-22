package com.example.tfc.factory.writer;

import com.example.tfc.factory.commons.Constants;
import com.example.tfc.factory.commons.dto.PanelDTO;
import com.example.tfc.factory.commons.dto.TypeScriptFunctionDTO;
import com.example.tfc.factory.commons.dto.TypeScriptImportDTO;
import com.example.tfc.factory.utils.ThymeleafUtils;
import com.example.tfc.factory.utils.TypeScriptTemplateUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.thymeleaf.context.Context;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Locale;
import java.util.stream.Collectors;

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

                Context ctx = new Context(Locale.US);
                ctx.setVariable("serviceName", name);
                ctx.setVariable("serviceUri", d.getUrl());

                body.append(ThymeleafUtils.tsTemplateEngine().process("http-get-call", ctx));

            });

            panelDTO.getComponent().getServiceCalls().forEach(call -> {
                Context ctx = new Context(Locale.US);
                ctx.setVariable("serviceName", call.getFunctionName());
                ctx.setVariable("servicePath", call.getPath());

                body.append(ThymeleafUtils.tsTemplateEngine().process(call.getTemplate(), ctx));
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
        String[] environment = {"environment"};
        panelDTO.getService().getImports().add(new TypeScriptImportDTO(environment, "../../environments/environment"));
    }
}

