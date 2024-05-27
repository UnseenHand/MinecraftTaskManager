package net.unseenhand.taskmanagermod.gui.components;

import net.minecraft.client.OptionInstance;
import net.minecraft.client.gui.components.AbstractButton;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.network.chat.Component;
import net.unseenhand.taskmanagermod.interfaces.Selectable;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;

public abstract class AbstractCheckbox extends AbstractButton implements Selectable {
    protected boolean selected;
    protected final SwitchCheckbox.OnValueChange onValueChange;

    public AbstractCheckbox(int x,
                            int y,
                            int width,
                            int height,
                            Component msg,
                            SwitchCheckbox.OnValueChange onChange) {
        super(x, y, width, height, msg);
        this.onValueChange = onChange;
    }

    public abstract static class BaseBuilder<T, B> {
        protected int x = 0;
        protected int y = 0;
        protected SwitchCheckbox.OnValueChange onValueChange = SwitchCheckbox.OnValueChange.NOP;
        protected boolean selected = false;
        @Nullable
        protected OptionInstance<Boolean> option = null;
        @Nullable
        protected Tooltip tooltip = null;

        abstract protected B self();

        public B pos(int x, int y) {
            this.x = x;
            this.y = y;
            return self();
        }

        public B onValueChange(SwitchCheckbox.OnValueChange func) {
            this.onValueChange = func;
            return self();
        }

        public B selected(boolean selected) {
            this.selected = selected;
            this.option = null;
            return self();
        }

        public B selected(@NotNull OptionInstance<Boolean> option) {
            this.option = option;
            this.selected = option.get();
            return self();
        }

        public B tooltip(Tooltip tooltip) {
            this.tooltip = tooltip;
            return self();
        }

        abstract public T build();
    }
}
