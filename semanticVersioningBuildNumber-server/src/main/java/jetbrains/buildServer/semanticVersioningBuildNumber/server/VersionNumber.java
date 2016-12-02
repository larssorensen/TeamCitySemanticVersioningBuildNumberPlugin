package jetbrains.buildServer.semanticVersioningBuildNumber.server;

import org.jetbrains.annotations.NotNull;

import java.util.Map;

public class VersionNumber {

    private static Integer INVALID_VALUE = -1;
    private Integer versionNumberValue = INVALID_VALUE;

    public VersionNumber(@NotNull String parameterName, @NotNull Map<String, String> params) {
        if (params != null && params.containsKey(parameterName)){
            this.versionNumberValue = parse(params.get(parameterName));
        }
    }

    public Integer getValue(){
        return versionNumberValue;
    }

    public Boolean isValid(){
        return this.versionNumberValue > INVALID_VALUE;
    }

    private Integer parse(String value){
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e){
            return INVALID_VALUE;
        }
    }
}
