package com.example.tfc.factory.writer;

import com.example.tfc.factory.commons.Constants;
import com.example.tfc.factory.commons.dto.PanelDTO;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.io.*;
import java.util.function.Consumer;

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

//            StreamGobbler streamGobbler =
//                    new StreamGobbler(process.getInputStream(), System.out::println);
//            Executors.newSingleThreadExecutor().submit(streamGobbler);
            int exitCode = process.waitFor();
            assert exitCode == 0;

            createComponentFolder(panelDTO);
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void createComponentFolder(PanelDTO panelDTO) {
        new File(Constants.FULL_COMPONENT_FOLDER_PATH + panelDTO.getName()).mkdirs();
    }

    private static class StreamGobbler implements Runnable {
        private InputStream inputStream;
        private Consumer<String> consumer;

        public StreamGobbler(InputStream inputStream, Consumer<String> consumer) {
            this.inputStream = inputStream;
            this.consumer = consumer;
        }

        @Override
        public void run() {
            new BufferedReader(new InputStreamReader(inputStream)).lines()
                    .forEach(consumer);
        }
    }
}
