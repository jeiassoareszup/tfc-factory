package com.example.tfc.factory.writer;

import com.example.tfc.factory.commons.Constants;
import com.example.tfc.factory.commons.dto.PanelDTO;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;

@Component
@Qualifier("projectWriter")
public class ProjectWriter implements Writer {

    @Override
    public void write(PanelDTO panelDTO) {

        try {
            ProcessBuilder builder = new ProcessBuilder();
            builder.directory(new File("/tmp"));
            builder.command(String.format(Constants.NEW_PROJECT_STRING_TEMPLATE, panelDTO.getName(), Constants.PROJECT_ROOT_FOLDER_NAME).split(" "));

            Process process = builder.start();

            int exitCode = process.waitFor();
            assert exitCode == 0;

            createComponentFolder(panelDTO);
            createServiceFolder();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void createComponentFolder(PanelDTO panelDTO) {
        new File(Constants.FULL_COMPONENT_FOLDER_PATH + panelDTO.getName()).mkdirs();
    }


    private void createServiceFolder() {
        new File(Constants.FULL_SERVICE_FOLDER_PATH).mkdirs();
    }
}
