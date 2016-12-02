package jetbrains.buildServer.semanticVersioningBuildNumber.server;

import jetbrains.buildServer.serverSide.*;
import jetbrains.buildServer.util.EventDispatcher;
import org.jetbrains.annotations.NotNull;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class VersionUpdater extends BuildServerAdapter {

    public VersionUpdater(@NotNull EventDispatcher<BuildServerListener> eventDispatcher)
    {
        eventDispatcher.addListener(this);
    }

    @Override
    public void buildStarted(@NotNull SRunningBuild runningBuild)
    {
        if (runningBuild == null) {
            throw new IllegalArgumentException("Argument 0 for @NotNull parameter of jetbrains/buildServer/lrz/serverPluginTest.buildStarted must not be null");
        }

        Log.startLogBlock(runningBuild);

        // If enabled
        if (runningBuild.getBuildFeaturesOfType(Util.FEATURE_TYPE).isEmpty()){
            Log.logInfo("Build feature '" + Util.FEATURE_TYPE + "' is disabled.", runningBuild);
            Log.endLogBlock(runningBuild);
            return;
        }

        // Get feature parameters
        Collection<SBuildFeatureDescriptor> features = runningBuild.getBuildFeaturesOfType(Util.FEATURE_TYPE);
        Map<String, String> params = new HashMap();
        if (!features.isEmpty()) {
            params.putAll((features.iterator().next()).getParameters());
        }
        params.putAll(runningBuild.getParametersProvider().getAll());

        // Get defined release branch parameter
        String releaseBranchName = Util.getReleaseBranch(params);
        Log.logInfo("Release branch name definition: " + releaseBranchName, runningBuild);

        // Get current branch
        boolean isRelease = false;
        Branch branch = runningBuild.getBranch();
        if (branch != null){
            Log.logInfo("Current branch name: " + branch.getName(), runningBuild);
            isRelease = branch.getName().equalsIgnoreCase(releaseBranchName);
        }

        // Get build parameters "MajorVersion", "MinorVersion", "PatchVersion" & build count
        VersionNumber major = new VersionNumber("MajorVersion", params);
        VersionNumber minor = new VersionNumber("MinorVersion", params);
        VersionNumber patch = new VersionNumber("PatchVersion", params);
        VersionNumber build = new VersionNumber("build.counter", params);

        if (!major.isValid()){
            Log.logWarning("No 'MajorVersion' parameter or value not defined.", runningBuild);
            Log.endLogBlock(runningBuild);
            return;
        }
        if (!minor.isValid()){
            Log.logWarning("No 'MinorVersion' parameter or value not defined.", runningBuild);
            Log.endLogBlock(runningBuild);
            return;
        }
        if (!patch.isValid()){
            Log.logWarning("No 'PatchVersion' parameter or value not defined.", runningBuild);
            Log.endLogBlock(runningBuild);
            return;
        }

        if (isRelease){

            Log.logInfo("Build is release build, setting release version format.", runningBuild);

            SBuildType buildType = runningBuild.getBuildType();
            if (buildType == null) {
                Log.logWarning("BuildType not found. Aborting.", runningBuild);
                Log.endLogBlock(runningBuild);
                return;
            }

            // Set new patch version & reset build counter for next build
            Integer nextPatchVersion = patch.getValue() + 1;
            buildType.addParameter(new SimpleParameter("PatchVersion", Integer.toString(nextPatchVersion)));
            BuildNumbers buildNumbers = buildType.getBuildNumbers();
            buildNumbers.setBuildNumberCounter(0);
            buildType.persist();
            Log.logInfo("'PatchVersion' parameter was updated to " + nextPatchVersion + " and build counter was reset", runningBuild);

            // Format build number
            String buildNumber = String.format("%1$s.%2$s.%3$s", major.getValue(), minor.getValue(), patch.getValue());
            runningBuild.setBuildNumber(buildNumber);
            Log.logInfo("BuildNumber was set to '" + buildNumber + "'.", runningBuild);

        }else{

            Log.logInfo("Build is not a release build, setting pre-release version format.", runningBuild);

            // Format build number
            String buildNumber = String.format("%1$s.%2$s.%3$s-DEV%4$03d",  major.getValue(), minor.getValue(), patch.getValue(), build.getValue());
            runningBuild.setBuildNumber(buildNumber);
            Log.logInfo("BuildNumber was set to '" + buildNumber + "'.", runningBuild);
        }
        Log.endLogBlock(runningBuild);
    }
}

