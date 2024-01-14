package net.minecraft.scoreboard;

import java.util.Arrays;
import java.util.Collection;
import java.util.Map;
import java.util.stream.Collectors;
import javax.annotation.Nullable;
import net.minecraft.util.text.IFormattableTextComponent;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;

public abstract class Team
{
    /**
     * Same as ==
     */
    public boolean isSameTeam(@Nullable Team other)
    {
        if (other == null)
        {
            return false;
        }
        else
        {
            return this == other;
        }
    }

    /**
     * Retrieve the name by which this team is registered in the scoreboard
     */
    public abstract String getName();

    public abstract IFormattableTextComponent func_230427_d_(ITextComponent p_230427_1_);

    /**
     * Checks whether members of this team can see other members that are invisible.
     */
    public abstract boolean getSeeFriendlyInvisiblesEnabled();

    /**
     * Checks whether friendly fire (PVP between members of the team) is allowed.
     */
    public abstract boolean getAllowFriendlyFire();

    /**
     * Gets the visibility flags for player name tags.
     */
    public abstract Visible getNameTagVisibility();

    /**
     * Gets the color for this team. The team color is used mainly for team kill objectives and team-specific setDisplay
     * usage; it does _not_ affect all situations (for instance, the prefix is used for the glowing effect).
     */
    public abstract TextFormatting getColor();

    public abstract Collection<String> getMembershipCollection();

    /**
     * Gets the visibility flags for player death messages.
     */
    public abstract Visible getDeathMessageVisibility();

    /**
     * Gets the rule to be used for handling collisions with members of this team.
     */
    public abstract CollisionRule getCollisionRule();

    public static enum CollisionRule
    {
        ALWAYS("always", 0),
        NEVER("never", 1),
        PUSH_OTHER_TEAMS("pushOtherTeams", 2),
        PUSH_OWN_TEAM("pushOwnTeam", 3);

        private static final Map<String, CollisionRule> nameMap = Arrays.stream(values()).collect(Collectors.toMap((p_199871_0_) -> {
            return p_199871_0_.name;
        }, (p_199870_0_) -> {
            return p_199870_0_;
        }));
        public final String name;
        public final int id;

        @Nullable
        public static CollisionRule getByName(String nameIn)
        {
            return nameMap.get(nameIn);
        }

        private CollisionRule(String nameIn, int idIn)
        {
            this.name = nameIn;
            this.id = idIn;
        }

        public ITextComponent getDisplayName()
        {
            return new TranslationTextComponent("team.collision." + this.name);
        }
    }

    public static enum Visible
    {
        ALWAYS("always", 0),
        NEVER("never", 1),
        HIDE_FOR_OTHER_TEAMS("hideForOtherTeams", 2),
        HIDE_FOR_OWN_TEAM("hideForOwnTeam", 3);

        private static final Map<String, Visible> nameMap = Arrays.stream(values()).collect(Collectors.toMap((p_199873_0_) -> {
            return p_199873_0_.internalName;
        }, (p_199872_0_) -> {
            return p_199872_0_;
        }));
        public final String internalName;
        public final int id;

        @Nullable
        public static Visible getByName(String nameIn)
        {
            return nameMap.get(nameIn);
        }

        private Visible(String nameIn, int idIn)
        {
            this.internalName = nameIn;
            this.id = idIn;
        }

        public ITextComponent getDisplayName()
        {
            return new TranslationTextComponent("team.visibility." + this.internalName);
        }
    }
}
