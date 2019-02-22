package com.example.tfc.factory.commons;

public final class Constants {

    public static final String PROJECT_ROOT_FOLDER_NAME = "/tfc-factory";
    public static final String NEW_PROJECT_STRING_TEMPLATE = "ng new %s --directory %s";
    public static final String EXTERNAL_JARS_FOLDER_PATH = "/Users/jeiassoares/class/";
    public static final String READ_ONLY_ATTRIBUTE_NAME = "[readonly]";
    public static final String HIDDEN_ATTRIBUTE_NAME = "[hidden]";
    public static final String DISABLED_ATTRIBUTE_NAME = "[disabled]";
    public static final String REQUIRED_ATTRIBUTE_NAME = "[required]";
    public static final String REGEX_REMOVE_SPECIAL_CHARACTERS= "[^a-zA-Z0-9]+";
    public static final String TEMP_FOLDER = "/tmp";
    public static final String APP_COMPONENT_FOLDER_PATH = "/src/app/components/";
    public static final String APP_COMPONENT_SERVICE_PATH = "/src/app/service/";
    public static final String FULL_COMPONENT_FOLDER_PATH = TEMP_FOLDER + PROJECT_ROOT_FOLDER_NAME + APP_COMPONENT_FOLDER_PATH;
    public static final String FULL_SERVICE_FOLDER_PATH = TEMP_FOLDER + PROJECT_ROOT_FOLDER_NAME + APP_COMPONENT_SERVICE_PATH;
    public static final String GLOBAL_SERVICE_NAME = "GlobalService";
    public static final String GLOBAL_SERVICE_FILE_NAME = "api.service.ts";
    public static final String VAR_PROMISE_SUFFIX = "Promise";
}
