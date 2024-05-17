package net.unseenhand.taskmanagermod.model;

import com.electronwill.nightconfig.core.UnmodifiableConfig;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;

public record Task(String id, String name, String description, byte status) {
    public static final Codec<Task> CODEC = RecordCodecBuilder.create(builder -> builder.group(
                    Codec.STRING.fieldOf("id").forGetter(Task::id),
                    Codec.STRING.fieldOf("name").forGetter(Task::name),
                    Codec.STRING.fieldOf("description").forGetter(Task::description),
                    Codec.BYTE.fieldOf("status").forGetter(Task::status))
            .apply(builder, Task::new));
    public static final StreamCodec<RegistryFriendlyByteBuf, Task> STREAM_CODEC = StreamCodec.ofMember(
            Task::write,
            Task::decode);

    public static Task decode(RegistryFriendlyByteBuf stream) {
        String id = stream.readUtf();
        String name  = stream.readUtf();
        String description = stream.readUtf();
        byte status = stream.readByte();
        return new Task(id, name, description, status);
    }

    public void write(RegistryFriendlyByteBuf data) {
        data.writeUtf(id);
        data.writeUtf(name);
        data.writeUtf(description);
        data.writeByte(status);
    }

    public static boolean inputsAreValid(String taskName, String taskDescription, String taskStatus) {
        return !taskName.isEmpty() && !taskDescription.isEmpty() && !taskStatus.isEmpty();
    }
}
