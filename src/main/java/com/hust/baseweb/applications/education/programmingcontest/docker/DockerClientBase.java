package com.hust.baseweb.applications.education.programmingcontest.docker;

import com.hust.baseweb.applications.education.programmingcontest.utils.ComputerLanguage;
import com.spotify.docker.client.DefaultDockerClient;
import com.spotify.docker.client.DockerClient;
import com.spotify.docker.client.DockerClient.ExecCreateParam;
import com.spotify.docker.client.DockerClient.ListContainersParam;
import com.spotify.docker.client.LogStream;
import com.spotify.docker.client.exceptions.DockerException;
import com.spotify.docker.client.messages.Container;
import com.spotify.docker.client.messages.ContainerConfig;
import com.spotify.docker.client.messages.ContainerCreation;
import com.spotify.docker.client.messages.ExecCreation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Configuration
public class DockerClientBase {
    @Value("${DOCKER_SERVER_HOST}")
    private String DOCKER_SERVER_HOST;

    private static DockerClient dockerClient;

    private static HashMap<String , String> m = new HashMap<>();


    public DockerClientBase() {
    }

    @Bean
    public void initDockerClientBase(){
        dockerClient = DefaultDockerClient.builder()
                .uri(URI.create(DOCKER_SERVER_HOST))
                .connectionPoolSize(100)
                .build();
        try {
            System.out.println("ping " + dockerClient.ping());
//            createGccContainer();
            containerExist();
        } catch (DockerException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void containerExist() throws DockerException, InterruptedException {
        List<Container> listContainerStart = dockerClient.listContainers(
                ListContainersParam.withStatusRunning(),
                ListContainersParam.filter("label", "names=leetcode")
        );
        for(Container container : listContainerStart){
            m.put(container.names().get(0), container.id());
        }
        List<Container> listContainersStop = dockerClient.listContainers(
                ListContainersParam.withStatusExited(),
                ListContainersParam.filter("label", "names=leetcode"));
        for(Container container : listContainersStop){
            m.put(container.names().get(0), container.id());
            dockerClient.startContainer(container.id());
        }
        List<Container> listContainersCreated = dockerClient.listContainers(
                ListContainersParam.withStatusCreated(),
                ListContainersParam.filter("label", "names=leetcode"));
        for(Container container : listContainersCreated){
            m.put(container.names().get(0), container.id());
            dockerClient.startContainer(container.id());
        }
        System.out.println(m.toString());
//        String containerId = m.get("/gcc");
//        String[] runCommand = {"sh", "-c", "while :; do sleep 1; done"};
//        ExecCreation runExecCreation = dockerClient.execCreate(
//                containerId, runCommand, DockerClient.ExecCreateParam.attachStdout(),
//                DockerClient.ExecCreateParam.attachStderr());
//        LogStream output = dockerClient.execStart(runExecCreation.id());
//        String execOutput = output.readFully();
//        System.out.println("exec output " + execOutput);

    }


    public String createGccContainer() throws DockerException, InterruptedException {
        Map<String,String> m = new HashMap<String,String>();
        m.put("names", "leetcode");
        ContainerConfig gccContainerConfig = ContainerConfig.builder()
                    .image("gcc:8.5-buster")
                .workingDir("/workdir")
                .hostname("test1")
                .cmd("sh", "-c", "while :; do sleep 1; done")
                .labels(m)
                .attachStdout(true)
                .attachStdin(true)
                .build();
        ContainerCreation gccCreation = dockerClient.createContainer(gccContainerConfig, "gcc");
        dockerClient.startContainer(gccCreation.id());
        return gccCreation.id();
    }

    public DockerClient getDockerClient(){
        return dockerClient;
    }

    public String runExecutable(ComputerLanguage.Languages languages, String dirName) throws DockerException, InterruptedException, IOException {
        String[] runCommand = {"bash", dirName+".sh"};
        String containerId;
        switch (languages){
            case CPP:
                containerId = m.get("/gcc");
                break;
            case JAVA:
                containerId = m.get("/java");
                break;
            case PYTHON3:
                containerId = m.get("/python3");
                break;
            case GOLANG:
                containerId = m.get("/golang");
                break;
            default:
                System.out.println("language err");
                return "err";
        }
        dockerClient.copyToContainer(new java.io.File("./temp_dir/"+dirName).toPath(), containerId, "/workdir/");
        ExecCreation runExecCreation = dockerClient.execCreate(
                containerId, runCommand, ExecCreateParam.attachStdout(),
                ExecCreateParam.attachStderr());
        LogStream output = dockerClient.execStart(runExecCreation.id());
        String execOutput = output.readFully();
        return execOutput;
    }
}
