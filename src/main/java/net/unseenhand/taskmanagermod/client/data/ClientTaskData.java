package net.unseenhand.taskmanagermod.client.data;

public class ClientTaskData {
    private static String id;
    private static String name;
    private static String description;
    private static byte status;

    public static void set(String id, String name, String description, byte status) {
        ClientTaskData.id = id;
        ClientTaskData.name = name;
        ClientTaskData.description = description;
        ClientTaskData.status = status;
    }

    public static String getId() {
        return id;
    }

    public static String getName() {
        return name;
    }

    public static String getDescription() {
        return description;
    }

    public static byte getStatus() {
        return status;
    }
}
