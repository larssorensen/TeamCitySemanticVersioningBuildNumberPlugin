package jetbrains.buildServer.semanticVersioningBuildNumber.server;

import jetbrains.buildServer.messages.DefaultMessagesInfo;
import jetbrains.buildServer.messages.Status;
import jetbrains.buildServer.serverSide.SRunningBuild;
import org.apache.log4j.Logger;
import org.jetbrains.annotations.NotNull;

public class Log
{
    private final static Logger LOG = Logger.getLogger(VersionUpdater.class.getName());

    public static void logInfo(@NotNull String message, SRunningBuild runningBuild){
        LOG.info(message);
        addBuildMessage(message, Status.NORMAL, runningBuild);
    }

    public static void logWarning(@NotNull String message, SRunningBuild runningBuild){
        LOG.warn(message);
        addBuildMessage(message, Status.WARNING, runningBuild);
    }

    public static void logError(String message, Exception ex, SRunningBuild runningBuild)
    {
        LOG.error(message, ex);
        addBuildMessage(message, Status.ERROR, runningBuild);
    }

    public static void startLogBlock(SRunningBuild runningBuild){
        runningBuild.addBuildMessage(DefaultMessagesInfo.createBlockStart("Semantic Version Updater", "server"));
    }

    public static void addBuildMessage(String message, Status status, SRunningBuild runningBuild){
        runningBuild.addBuildMessage(DefaultMessagesInfo.createTextMessage(message, status));
    }

    public static void endLogBlock(SRunningBuild runningBuild){
        runningBuild.addBuildMessage(DefaultMessagesInfo.createBlockEnd("Semantic Version Updater", "server"));
    }
}