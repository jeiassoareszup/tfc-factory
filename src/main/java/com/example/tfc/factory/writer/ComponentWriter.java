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
@Qualifier("componentWriter")
public class ComponentWriter implements Writer {

    @Override
    public void write(PanelDTO panelDTO) {

        panelDTO.getComponent().setFileName(panelDTO.getName() + ".component.ts");
        Path path = Paths.get(Constants.FULL_COMPONENT_FOLDER_PATH + panelDTO.getName() + "/" + panelDTO.getComponent().getFileName());

        try {

            StringBuilder builder = new StringBuilder();

            panelDTO.getComponent().getImports().add(getDefaultImpot());
            panelDTO.getComponent().getImports().add(new TypeScriptImportDTO(new String[]{Constants.GLOBAL_SERVICE_NAME}, "../../service/" + Constants.GLOBAL_SERVICE_FILE_NAME.substring(0, Constants.GLOBAL_SERVICE_FILE_NAME.length()-3)));

            if(panelDTO.getComponent().getFunctions().stream().noneMatch(f -> "ngOnInit".equals(f.getName()))){
                panelDTO.getComponent().getFunctions().add(getOnInit());
            }

            panelDTO.getComponent().getImports().forEach(i -> builder.append(TypeScriptTemplateUtils.getImport(i)));

            builder.append("\n");
            builder.append(TypeScriptTemplateUtils.getComponentDeclaration("app-" + panelDTO.getName(), "./"+ panelDTO.getHtml().getFileName(), null));

            StringBuilder body = new StringBuilder();

            body.append(TypeScriptTemplateUtils.getConstructor(Constants.GLOBAL_SERVICE_NAME));
            builder.append("\n");

            panelDTO.getComponent().getFields().forEach(f -> body.append(TypeScriptTemplateUtils.getFieldDeclaration(f)));
            panelDTO.getComponent().getFunctions().forEach(f -> body.append(TypeScriptTemplateUtils.getFunction(f)));

            builder.append(TypeScriptTemplateUtils.getClassDeclaration( Character.toUpperCase(panelDTO.getName().charAt(0)) + panelDTO.getName().substring(1) + "Component", body.toString(), true));
            Files.write(path, builder.toString().getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private TypeScriptImportDTO getDefaultImpot() {
        String names[] = {"Component", "OnInit"};
        return new TypeScriptImportDTO(names, "@angular/core");
    }

    private TypeScriptFunctionDTO getOnInit(){
        return new TypeScriptFunctionDTO("", "ngOnInit", null, "", null);
    }
}
