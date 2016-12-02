package jetbrains.buildServer.semanticVersioningBuildNumber.server;

    import java.util.HashMap;
    import java.util.Map;
    import javax.servlet.http.HttpServletRequest;
    import javax.servlet.http.HttpServletResponse;
    import jetbrains.buildServer.controllers.BaseController;
    import jetbrains.buildServer.serverSide.BuildFeature;
    import jetbrains.buildServer.web.openapi.PluginDescriptor;
    import jetbrains.buildServer.web.openapi.WebControllerManager;
    import org.jetbrains.annotations.NotNull;
    import org.springframework.web.servlet.ModelAndView;

public class VersionUpdaterBuildFeature extends BuildFeature {

    private final String myEditUrl;

    public VersionUpdaterBuildFeature(@NotNull final PluginDescriptor descriptor, @NotNull final WebControllerManager web) {

        final String jsp = descriptor.getPluginResourcesPath("VersionUpdaterSettings.jsp");
        final String html = descriptor.getPluginResourcesPath("VersionUpdaterSettings.html");

        web.registerController(html, new BaseController() {
            @Override
            protected ModelAndView doHandle(@NotNull final HttpServletRequest request, @NotNull final HttpServletResponse response) throws Exception {
                final ModelAndView mv = new ModelAndView(jsp);
                mv.getModel().put("requestUrl", html);
                mv.getModel().put("buildTypeId", getBuildTypeIdParameter(request));
                return mv;
            }
        });

        myEditUrl = html;
    }

    private String getBuildTypeIdParameter(final HttpServletRequest request) {
        return request.getParameter("id");
    }

    @NotNull
    @Override
    public String getType() {
        return Util.FEATURE_TYPE;
    }

    @NotNull
    @Override
    public String getDisplayName() {
        return "Semantic versioning build numbers";
    }

    @Override
    public String getEditParametersUrl() {
        return myEditUrl;
    }

    @Override
    public boolean isMultipleFeaturesPerBuildTypeAllowed() {
        return false;
    }

    @NotNull
    @Override
    public String describeParameters(@NotNull final Map<String, String> params) {

        StringBuilder result = new StringBuilder();
        result.append("Updates %build.number% with a Semantic Versioning version number based on configuration parameters MajorVersion, MinorVersion and PatchVersion.\n");
        result.append("Selected release branch name is '").append(Util.getReleaseBranch(params)).append("'\n");
        return result.toString();
    }

    @Override
    public Map<String, String> getDefaultParameters() {
        final Map<String, String> defaults = new HashMap<String, String>(1);
        defaults.put(Util.PARAMETER_RELEASE_BRANCH, Util.DEFAULT_RELEASE_BRANCH);
        return defaults;
    }
}