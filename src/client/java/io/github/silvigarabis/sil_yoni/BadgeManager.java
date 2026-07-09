package io.github.silvigarabis.sil_yoni;


import io.github.apace100.apoli.power.PowerType;
import io.github.apace100.calio.data.SerializableData;
import io.github.apace100.calio.data.SerializableDataTypes;
import io.github.apace100.origins.Origins;
import io.github.apace100.origins.badge.Badge;
import io.github.apace100.origins.badge.BadgeFactory;
import io.github.apace100.origins.integration.AutoBadgeCallback;
import io.github.silvigarabis.sil_yoni.power.detection.ActiveCooldownPower;
import io.github.silvigarabis.sil_yoni.power.detection.TogglePower;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.tooltip.OrderedTextTooltipComponent;
import net.minecraft.client.gui.tooltip.TooltipComponent;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.text.OrderedText;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import java.util.LinkedList;
import java.util.List;

public class BadgeManager {
    private static final Identifier ACTIVE_BADGE_SPRITE = Origins.identifier("textures/gui/badge/active.png");

    public static void createAutoBadges(Identifier powerId, PowerType<?> powerType, List<Badge> badgeList) {
        var power = powerType.create(null);
        if (power instanceof ActiveCooldownPower active) {
            for (var key : active.getUseKeys()){
                for (var pattern : key.triggers()){
                    var badge = new KeybindBadge(ACTIVE_BADGE_SPRITE, pattern.translationKey(), key.key());
                    badgeList.add(badge);
                }
            }
        } else if (power instanceof TogglePower toggle) {
            for (var key : toggle.getUseKeys()){
                for (var pattern : key.triggers()){
                    var badge = new KeybindBadge(ACTIVE_BADGE_SPRITE, pattern.toggleTranslationKey(), key.key());
                    badgeList.add(badge);
                }
            }
        }
    }

    public static void init(){
        AutoBadgeCallback.EVENT.register(BadgeManager::createAutoBadges);
    }

    public record KeybindBadge(Identifier spriteId, String translationKey, String keybindResourceKey) implements Badge {

        public KeybindBadge(SerializableData.Instance instance) {
            this(instance.getId("sprite"), instance.getString("translation"), instance.getString("keybind"));
        }

        @Override
        public boolean hasTooltip() {
            return true;
        }

        public static void addLines(List<TooltipComponent> tooltips, Text text, TextRenderer textRenderer, int widthLimit) {
            if(textRenderer.getWidth(text) > widthLimit) {
                for(OrderedText orderedText : textRenderer.wrapLines(text, widthLimit)) {
                    tooltips.add(new OrderedTextTooltipComponent(orderedText));
                }
            } else {
                tooltips.add(new OrderedTextTooltipComponent(text.asOrderedText()));
            }
        }

        @Override
        public List<TooltipComponent> getTooltipComponents(PowerType<?> powerType, int widthLimit, float time, TextRenderer textRenderer) {
            List<TooltipComponent> tooltips = new LinkedList<>();
            Text keyText;
            keyText = Text.literal("[")
                    .append(KeyBinding.getLocalizedName(keybindResourceKey).get())
                    .append(Text.of("]"));

            addLines(tooltips, Text.translatable(translationKey, keyText), textRenderer, widthLimit);
            return tooltips;
        }

        @Override
        public SerializableData.Instance toData(SerializableData.Instance instance) {
            instance.set("sprite", spriteId);
            instance.set("translation", translationKey);
            instance.set("keybind", keybindResourceKey);
            return instance;
        }

        @Override
        public BadgeFactory getBadgeFactory() {
            return KEYBIND;
        }
    }

    public static final BadgeFactory KEYBIND = new BadgeFactory(SilYoniMod.identifier("keybind"),
            new SerializableData()
                    .add("sprite", SerializableDataTypes.IDENTIFIER)
                    .add("translation", SerializableDataTypes.STRING),
            KeybindBadge::new);
}
