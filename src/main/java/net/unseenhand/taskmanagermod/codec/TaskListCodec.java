package net.unseenhand.taskmanagermod.codec;

import com.mojang.datafixers.util.Pair;
import com.mojang.datafixers.util.Unit;
import com.mojang.serialization.*;
import net.unseenhand.taskmanagermod.model.Task;
import org.apache.commons.lang3.mutable.MutableObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

public class TaskListCodec implements Codec<List<Task>> {
    @Override
    public <T> DataResult<Pair<List<Task>, T>> decode(DynamicOps<T> ops, T input) {
        return ops.getList(input).setLifecycle(Lifecycle.stable()).flatMap(stream -> {
            var elements = new ArrayList<Task>();
            final Stream.Builder<T> failed = Stream.builder();
            // TODO: AtomicReference.getPlain/setPlain in java9+
            final MutableObject<DataResult<Unit>> result = new MutableObject<>(
                    DataResult.success(Unit.INSTANCE, Lifecycle.stable()));

            stream.accept(t -> {
                if (ops.emptyMap().equals(t)) {
                    elements.add(null);
                } else {
                    DataResult<Pair<Task, T>> element = Task.CODEC.decode(ops, t);
                    element.error().ifPresent(e -> failed.add(t));
                    result.setValue(result.getValue().apply2stable((r, v) -> {
                        elements.add(v.getFirst());
                        return r;
                    }, element));
                }
            });

            final T errors = ops.createList(failed.build());

            final Pair<List<Task>, T> pair = Pair.of(Collections.unmodifiableList(elements), errors);

            return result.getValue().map(unit -> pair).setPartial(pair);
        });
    }

    @Override
    public <T> DataResult<T> encode(List<Task> input, DynamicOps<T> ops, T prefix) {
        final ListBuilder<T> builder = ops.listBuilder();

        for (final Task a : input) {
            if (a == null) {
                builder.add(ops.emptyMap());
            } else {
                builder.add(Task.CODEC.encodeStart(ops, a));
            }
        }

        return builder.build(prefix);
    }
}
