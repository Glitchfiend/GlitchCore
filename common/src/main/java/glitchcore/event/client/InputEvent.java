/*******************************************************************************
 * Copyright 2023, the Glitchfiend Team.
 * All rights reserved.
 ******************************************************************************/
package glitchcore.event.client;

import glitchcore.event.Event;

public abstract class InputEvent extends Event
{
    // See InputConstants
    public static class Key extends InputEvent
    {
        private final int key;
        private final int keyCode;
        private final int modifiers;
        private boolean handledDebugKey;

        public Key(int key, int keyCode, int modifiers, boolean handledDebugKey)
        {
            this.key = key;
            this.keyCode = keyCode;
            this.modifiers = modifiers;
            this.handledDebugKey = handledDebugKey;
        }

        public int getKey()
        {
            return this.key;
        }

        public int getKeyCode()
        {
            return this.keyCode;
        }

        public int getModifiers()
        {
            return this.modifiers;
        }

        public boolean getHandledDebugKey()
        {
            return this.handledDebugKey;
        }

        public void setHandledDebugKey(boolean value)
        {
            this.handledDebugKey = value;
        }
    }
}
