package com.dci.intellij.dbn.debugger.jdwp.config;

import org.jetbrains.annotations.NotNull;

import com.dci.intellij.dbn.debugger.jdbc.config.DBProgramRunProfileState;
import com.intellij.debugger.engine.RemoteDebugProcessHandler;
import com.intellij.execution.DefaultExecutionResult;
import com.intellij.execution.ExecutionException;
import com.intellij.execution.ExecutionResult;
import com.intellij.execution.Executor;
import com.intellij.execution.configurations.ConfigurationPerRunnerSettings;
import com.intellij.execution.configurations.RunnerSettings;
import com.intellij.execution.runners.ExecutionEnvironment;
import com.intellij.execution.runners.ProgramRunner;
import com.intellij.openapi.project.Project;


public class DBMethodJdwpRunProfileState extends DBProgramRunProfileState {
    public DBMethodJdwpRunProfileState(ExecutionEnvironment environment) {
        super(environment);
    }

    public ExecutionResult execute(Executor executor, @NotNull ProgramRunner runner) throws ExecutionException {
        Project project = getEnvironment().getProject();
        RemoteDebugProcessHandler processHandler = new RemoteDebugProcessHandler(project);
        return new DefaultExecutionResult(null, processHandler);
    }

    public RunnerSettings getRunnerSettings() {
        return null;
    }

    public ConfigurationPerRunnerSettings getConfigurationSettings() {
        return null;
    }
}