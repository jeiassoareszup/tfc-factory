package com.example.tfc.factory.utils;

import com.example.tfc.factory.commons.dto.TypeScriptFieldDTO;
import com.example.tfc.factory.commons.dto.TypeScriptFunctionDTO;
import com.example.tfc.factory.commons.dto.TypeScriptImportDTO;
import org.apache.commons.lang3.StringUtils;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;

import java.util.HashSet;
import java.util.Locale;
import java.util.stream.Collectors;

public final class TypeScriptTemplateUtils {

    public static String getIf(String condition, String body, String elseBody) {

        if (StringUtils.isEmpty(condition) || StringUtils.isEmpty(body)) {
            return "";
        }

        StringBuilder builder = new StringBuilder();

        builder.append("if (%s) {  \n");
        builder.append("   %s\n");

        if (!StringUtils.isEmpty(elseBody)) {
            builder.append("} else {\n");
            builder.append("   %s\n");
            builder.append("}\n");
        } else {
            builder.append("}\n");
        }

        return String.format(builder.toString(), condition, body, elseBody);
    }

    public static String getFunction(TypeScriptFunctionDTO functionDTO) {
        if (functionDTO == null) {
            return "";
        }
        StringBuilder builder = new StringBuilder();

        builder.append("%s %s(%s) {\n");
        builder.append("    %s\n");

        if (!StringUtils.isEmpty(functionDTO.getRtrn())) {
            builder.append("    return %s;\n");
        }

        builder.append("}\n");


        return String.format(builder.toString(),
                functionDTO.getAccess(),
                functionDTO.getName(),
                functionDTO.getParams() == null ? "" : String.join(", ", functionDTO.getParams()),
                functionDTO.getBody(),
                functionDTO.getRtrn());
    }

//    public static void main(String[] args) {
//        StringBuilder all = new StringBuilder();
//
//        String[] names = {"Component"};
//        TypeScriptImportDTO importDTO = new TypeScriptImportDTO(names, "@angular/core");
//
//        all.append(TypeScriptTemplateUtils.getImport(importDTO));
//        all.append("\n");
//
//        String[] s = {"./create.css"};
//        TypeScriptComponentDeclarationDTO declarationDTO = new TypeScriptComponentDeclarationDTO("app-create", "./create.html", s);
//
//        all.append(TypeScriptTemplateUtils.getComponentDeclaration(declarationDTO));
//
//        StringBuilder body = new StringBuilder();
//
//        TypeScriptFieldDTO fieldDTO = new TypeScriptFieldDTO("title", "\"test titulo\"");
//        String[] p = {"a", "b"};
//
//        TypeScriptFunctionDTO d = new TypeScriptFunctionDTO("add", p, "return a + b;");
//
//        body.append(TypeScriptTemplateUtils.getFieldDeclaration(fieldDTO));
//        body.append(TypeScriptTemplateUtils.getFunction(d));
//
//        all.append(TypeScriptTemplateUtils.getClassDeclaration("CreateComponent", body.toString(), false));
//
//
//        System.out.println(all.toString());
//        System.out.println(getConstructor("Component", "HttpClient"));
//    }

    public static String getImport(TypeScriptImportDTO importDTO) {

        if (importDTO == null) {
            return "";
        }

        StringBuilder builder = new StringBuilder();

        builder.append("import { %s } from '%s';");
        builder.append("\n");

        return String.format(builder.toString(), String.join(", ", importDTO.getName()), importDTO.getPath());
    }

    public static String getFieldDeclaration(TypeScriptFieldDTO fieldDTO) {

        if (fieldDTO == null) {
            return "";
        }

        StringBuilder builder = new StringBuilder();
        builder.append("%s = %s;");
        builder.append("\n");

        return String.format(builder.toString(), fieldDTO.getName(), fieldDTO.getValue());
    }

    public static String getInjectableDeclaration() {

        StringBuilder builder = new StringBuilder();

        builder.append("@Injectable({\n");
        builder.append("    providedIn:  'root'\n");
        builder.append("})\n");

        return builder.toString();
    }

    public static String getComponentDeclaration(String selector, String templateUrl, String[] styleUrls) {

        StringBuilder builder = new StringBuilder();

        builder.append("@Component({\n");
        builder.append("    selector: '%s',\n");
        builder.append("    templateUrl: '%s'");

        if (styleUrls != null && styleUrls.length > 0) {
            builder.append(",\n");
            builder.append("    styleUrls: ['%s']\n");
            builder.append("})\n");
            return String.format(builder.toString(), selector, templateUrl, String.join(", ", styleUrls));
        }

        builder.append("})\n");
        return String.format(builder.toString(), selector, templateUrl);
    }

    public static String getAttribution(TypeScriptFieldDTO fieldDTO) {
        return "this." + getFieldDeclaration(fieldDTO);
    }

    public static String getClassDeclaration(String name, String body, boolean withInit) {

        if (StringUtils.isEmpty(name)) {
            return "";
        }

        StringBuilder builder = new StringBuilder();

        if (withInit) {
            builder.append("export class %s implements OnInit {\n");
        } else {
            builder.append("export class %s {\n");
        }

        builder.append("\n  %s\n");
        builder.append("}\n");

        return String.format(builder.toString(), name, body);

    }

    public static String getConstructor(String... params) {
        if (params == null || params.length == 0) {
            return "";
        }

        String[] args = new String[params.length];

        for (int i = 0; i < params.length; i++) {
            args[i] = String.format("private  %s:  %s", Character.toLowerCase(params[i].charAt(0)) + params[i].substring(1), params[i]);
        }

        return String.format("constructor(%s) {}\n", String.join(", ", args));
    }

    public static String getFunctionCall(String var, String functionName, boolean commented, boolean inner, String... params) {
        StringBuilder builder = new StringBuilder("    ");
//        if (commented) {
//            builder.append("// ");
//        }

        String param = params == null || params.length == 0 ? "" : String.join(", ", params);

        if (!StringUtils.isEmpty(var)) {

            builder.append("let ");
            builder.append(var);
            builder.append(" = ");
        }

        if (inner) {
            builder.append("this.");
        }

        builder.append(functionName);
        builder.append("(");
        if(StringUtils.contains(functionName, "globalService")) {
            builder.append("JSON.parse('{").append(StringUtils.replace(param, "'", "\"")).append("}')");
        } else {
            builder.append(param);
        }

        builder.append(");\n");

        return builder.toString();
    }

    public static String getFunctionResult(String promise, HashSet<String> results) {

        Context ctx = new Context(Locale.US);
        ctx.setVariable("promiseName", promise);
        ctx.setVariable("attributionList",
                results.stream()
                        .map(s -> {
                            if (StringUtils.contains(s, "*")) {
                                return StringUtils.split(s, "*");
                            } else {
                                return new String[]{s, s};
                            }
                        })
                        .map(sT -> "this." + sT[0] + " = " + (!StringUtils.contains(sT[1], "'") ? "result."+sT[1] : "''"))
                        .collect(Collectors.toList())
        );

        return ThymeleafUtils.tsTemplateEngine().process("service-response-bind", ctx);

    }

    public static String getNgModule(String[] declarations, String[] imports, String[] providers, String[] bootstrap) {
        StringBuilder builder = new StringBuilder();

        builder.append("@NgModule({\n");
        builder.append("    declarations: [%s], \n");
        builder.append("    imports: [%s], \n");
        builder.append("    providers: [%s], \n");
        builder.append("    bootstrap: [%s] \n");
        builder.append("})\n");

        return String.format(builder.toString(), String.join(", ", declarations), String.join(", ", imports), String.join(", ", providers), String.join(", ", bootstrap));
    }


}
