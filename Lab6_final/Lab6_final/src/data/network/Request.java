package data.network;


import data.Worker;

import java.io.Serializable;

public class Request implements Serializable {
    private String command;
    private Worker worker;

    public Request(String command) {
        this.command = command;
    }

    public void setWorker(Worker worker) {
        this.worker = worker;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    public String getCommand() {
        return command;
    }

    public Worker getWorker() {
        return worker;
    }


    @Override
    public String toString() {
        return "Request{" +
                "command='" + command + '\'' +
                '}';
    }
}
