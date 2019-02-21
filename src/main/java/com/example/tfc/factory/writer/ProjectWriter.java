package com.example.tfc.factory.writer;

import com.example.tfc.factory.commons.Constants;
import com.example.tfc.factory.commons.dto.PanelDTO;
import com.example.tfc.factory.commons.dto.TypeScriptImportDTO;
import com.example.tfc.factory.utils.TypeScriptTemplateUtils;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

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

    public void bindFiles(PanelDTO panelDTO) {
        moveStyle();
        createModule(panelDTO);
        createAppHtml(panelDTO);
    }

    private void createAppHtml(PanelDTO panelDTO) {
        Path path = Paths.get(Constants.TEMP_FOLDER + Constants.PROJECT_ROOT_FOLDER_NAME + "/src/app/app.component.html");

        try {
            Files.write(path, String.format("<div style=\"text-align:center\">\n" +
                    "  <app-%s></app-%s>", panelDTO.getName(), panelDTO.getName()).getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void moveStyle() {
        Path path = Paths.get(Constants.TEMP_FOLDER + Constants.PROJECT_ROOT_FOLDER_NAME + "/src/styles.css");

        String style = "table {\n" +
                "  font-family: \"Trebuchet MS\", Arial, Helvetica, sans-serif;\n" +
                "  border-collapse: collapse;\n" +
                "  width: 100%;\n" +
                "}\n" +
                "\n" +
                "table td, table th {\n" +
                "  border: 1px solid #ddd;\n" +
                "  padding: 8px;\n" +
                "}\n" +
                "\n" +
                "table tr:nth-child(even){background-color: #f2f2f2;}\n" +
                "\n" +
                "table tr:hover {background-color: #ddd;}\n" +
                "\n" +
                "table th {\n" +
                "  padding-top: 12px;\n" +
                "  padding-bottom: 12px;\n" +
                "  text-align: left;\n" +
                "  background-color: #ec001e;\n" +
                "  color: white;\n" +
                "}\n" +
                "\n" +
                "button {\n" +
                "  /*background-color: #ec001e;*/\n" +
                "  border: none;\n" +
                "  color: white;\n" +
                "  padding: 10px 20px;\n" +
                "  text-align: center;\n" +
                "  text-decoration: none;\n" +
                "  font-size: 16px;\n" +
                "  margin: 4px 2px;\n" +
                "  cursor: pointer;\n" +
                "}\n" +
                "\n" +
                "input[type=text], select {\n" +
                "  width: 20%;\n" +
                "  padding: 12px 20px;\n" +
                "  margin: 8px 0;\n" +
                "  display: inline-block;\n" +
                "  border: 1px solid #ccc;\n" +
                "  border-radius: 4px;\n" +
                "  box-sizing: border-box;\n" +
                "}\n" +
                "\n" +
                "input[type=submit] {\n" +
                "  width: 100%;\n" +
                "  background-color: #4CAF50;\n" +
                "  color: white;\n" +
                "  padding: 14px 20px;\n" +
                "  margin: 8px 0;\n" +
                "  border: none;\n" +
                "  border-radius: 4px;\n" +
                "  cursor: pointer;\n" +
                "}\n" +
                "\n" +
                "input[type=submit]:hover {\n" +
                "  background-color: #45a049;\n" +
                "}\n" +
                "\n" +
                "\n" +
                ".bg-green {background-color: #4CAF50;} /* Green */\n" +
                ".bg-blue {background-color: #008CBA;} /* Blue */\n" +
                ".bg-red {background-color: #ec001e;} /* Red */\n" +
                ".bg-gray {background-color: #e7e7e7; color: black;} /* Gray */\n" +
                ".bg-black {background-color: #555555;} /* Black */\n" +
                "\n" +
                "\n" +
                "ul {\n" +
                "  list-style-type: none;\n" +
                "  margin: 0;\n" +
                "  padding: 0;\n" +
                "  overflow: hidden;\n" +
                "  background-color: #333;\n" +
                "}\n" +
                "\n" +
                "li {\n" +
                "  float: left;\n" +
                "}\n" +
                "\n" +
                "li a {\n" +
                "  display: block;\n" +
                "  color: white;\n" +
                "  text-align: center;\n" +
                "  padding: 14px 16px;\n" +
                "  text-decoration: none;\n" +
                "}\n" +
                "\n" +
                "li a:hover:not(.active) {\n" +
                "  background-color: #111;\n" +
                "}\n" +
                "\n" +
                ".active {\n" +
                "  background-color: #4CAF50;\n" +
                "}\n" +
                "\n" +
                "body {\n" +
                "  margin: 0;\n" +
                "  width: 100%;\n" +
                "  height: 100%;\n" +
                "}\n" +
                "\n" +
                "html {\n" +
                "  width: 100%;\n" +
                "  height: 100%;\n" +
                "}\n";

        try {
            Files.write(path, style.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void createModule(PanelDTO panelDTO) {
        Path path = Paths.get(Constants.TEMP_FOLDER + Constants.PROJECT_ROOT_FOLDER_NAME + "/src/app/app.module.ts");

        try {
            StringBuilder builder = new StringBuilder();

            setDefaultImport(panelDTO);

            String[] component = {Character.toUpperCase(panelDTO.getName().charAt(0)) + panelDTO.getName().substring(1) + "Component"};
            String componentPath = "./components/" + panelDTO.getName() + "/" + panelDTO.getComponent().getFileName().substring(0, panelDTO.getComponent().getFileName().length() - 3);
            panelDTO.getService().getImports().add(new TypeScriptImportDTO(component, componentPath));

            panelDTO.getService().getImports().forEach(i -> builder.append(TypeScriptTemplateUtils.getImport(i)));
            builder.append("\n");

            builder.append(TypeScriptTemplateUtils.getNgModule(new String[]{"AppComponent", "" + component[0]+""}, new String[]{"BrowserModule", "HttpClientModule", "FormsModule"}, new String[]{"GlobalService"}, new String[]{"AppComponent"}));

            builder.append("\n");
            builder.append(TypeScriptTemplateUtils.getClassDeclaration("AppModule", "", false));

            Files.write(path, builder.toString().getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void setDefaultImport(PanelDTO panelDTO) {
        panelDTO.getService().getImports().add(new TypeScriptImportDTO(new String[]{"BrowserModule"}, "@angular/platform-browser"));
        panelDTO.getService().getImports().add(new TypeScriptImportDTO(new String[]{"NgModule"}, "@angular/core"));
        panelDTO.getService().getImports().add(new TypeScriptImportDTO(new String[]{"HttpClientModule"}, "@angular/common/http"));
        panelDTO.getService().getImports().add(new TypeScriptImportDTO(new String[]{"FormsModule"}, "@angular/forms"));
        panelDTO.getService().getImports().add(new TypeScriptImportDTO(new String[]{"AppComponent"}, "./app.component"));
        panelDTO.getService().getImports().add(new TypeScriptImportDTO(new String[]{"GlobalService"}, "./service/api.service"));
    }
}
