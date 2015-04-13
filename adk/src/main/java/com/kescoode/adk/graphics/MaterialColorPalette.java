package com.kescoode.adk.graphics;

import android.graphics.Color;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * 随机产生相应的颜色值
 *
 * @author Kesco Lin
 */
public class MaterialColorPalette {

    private static List<String> MATERIALCOLORS = new ArrayList<String>(250);

    static {
        /* Red */
        // MATERIALCOLORS.add("#e51c23");
        MATERIALCOLORS.add("#fde0dc");
        MATERIALCOLORS.add("#f9bdbb");
        MATERIALCOLORS.add("#f69988");
        MATERIALCOLORS.add("#f36c60");
        MATERIALCOLORS.add("#e84e40");
        MATERIALCOLORS.add("#e51c23");
        MATERIALCOLORS.add("#dd191d");
        MATERIALCOLORS.add("#d01716");
        MATERIALCOLORS.add("#c41411");
        MATERIALCOLORS.add("#b0120a");
        MATERIALCOLORS.add("#ff7997");
        MATERIALCOLORS.add("#ff5177");
        MATERIALCOLORS.add("#ff2d6f");
        MATERIALCOLORS.add("#e00032");

        /* Pink */
        // MATERIALCOLORS.add("#e91e63");
        MATERIALCOLORS.add("#fce4ec");
        MATERIALCOLORS.add("#f8bbd0");
        MATERIALCOLORS.add("#f48fb1");
        MATERIALCOLORS.add("#f06292");
        MATERIALCOLORS.add("#ec407a");
        MATERIALCOLORS.add("#e91e63");
        MATERIALCOLORS.add("#d81b60");
        MATERIALCOLORS.add("#c2185b");
        MATERIALCOLORS.add("#ad1457");
        MATERIALCOLORS.add("#880e4f");
        MATERIALCOLORS.add("#ff80ab");
        MATERIALCOLORS.add("#ff4081");
        MATERIALCOLORS.add("#f50057");
        MATERIALCOLORS.add("#c51162");

        /* Purple */
        // MATERIALCOLORS.add("#9c27b0");
        MATERIALCOLORS.add("#f3e5f5");
        MATERIALCOLORS.add("#e1bee7");
        MATERIALCOLORS.add("#ce93d8");
        MATERIALCOLORS.add("#ba68c8");
        MATERIALCOLORS.add("#ab47bc");
        MATERIALCOLORS.add("#9c27b0");
        MATERIALCOLORS.add("#8e24aa");
        MATERIALCOLORS.add("#7b1fa2");
        MATERIALCOLORS.add("#6a1b9a");
        MATERIALCOLORS.add("#4a148c");
        MATERIALCOLORS.add("#ea80fc");
        MATERIALCOLORS.add("#e040fb");
        MATERIALCOLORS.add("#d500f9");
        MATERIALCOLORS.add("#aa00ff");

        /* Deep Purple */
        // MATERIALCOLORS.add("#673ab7");
        MATERIALCOLORS.add("#ede7f6");
        MATERIALCOLORS.add("#d1c4e9");
        MATERIALCOLORS.add("#b39ddb");
        MATERIALCOLORS.add("#9575cd");
        MATERIALCOLORS.add("#7e57c2");
        MATERIALCOLORS.add("#673ab7");
        MATERIALCOLORS.add("#5e35b1");
        MATERIALCOLORS.add("#512da8");
        MATERIALCOLORS.add("#4527a0");
        MATERIALCOLORS.add("#311b92");
        MATERIALCOLORS.add("#b388ff");
        MATERIALCOLORS.add("#7c4dff");
        MATERIALCOLORS.add("#651fff");
        MATERIALCOLORS.add("#6200ea");

        /* Indigo */
        // MATERIALCOLORS.add("#3f51b5");
        MATERIALCOLORS.add("#e8eaf6");
        MATERIALCOLORS.add("#c5cae9");
        MATERIALCOLORS.add("#9fa8da");
        MATERIALCOLORS.add("#7986cb");
        MATERIALCOLORS.add("#5c6bc0");
        MATERIALCOLORS.add("#3f51b5");
        MATERIALCOLORS.add("#3949ab");
        MATERIALCOLORS.add("#303f9f");
        MATERIALCOLORS.add("#283593");
        MATERIALCOLORS.add("#1a237e");
        MATERIALCOLORS.add("#8c9eff");
        MATERIALCOLORS.add("#536dfe");
        MATERIALCOLORS.add("#3d5afe");
        MATERIALCOLORS.add("#304ffe");

        /* Blue */
        // MATERIALCOLORS.add("#5677fc");
        MATERIALCOLORS.add("#e7e9fd");
        MATERIALCOLORS.add("#d0d9ff");
        MATERIALCOLORS.add("#afbfff");
        MATERIALCOLORS.add("#91a7ff");
        MATERIALCOLORS.add("#738ffe");
        MATERIALCOLORS.add("#5677fc");
        MATERIALCOLORS.add("#4e6cef");
        MATERIALCOLORS.add("#455ede");
        MATERIALCOLORS.add("#3b50ce");
        MATERIALCOLORS.add("#2a36b1");
        MATERIALCOLORS.add("#a6baff");
        MATERIALCOLORS.add("#6889ff");
        MATERIALCOLORS.add("#4d73ff");
        MATERIALCOLORS.add("#4d69ff");

        /* Light Blue */
        // MATERIALCOLORS.add("#03a9f4");
        MATERIALCOLORS.add("#e1f5fe");
        MATERIALCOLORS.add("#b3e5fc");
        MATERIALCOLORS.add("#81d4fa");
        MATERIALCOLORS.add("#4fc3f7");
        MATERIALCOLORS.add("#29b6f6");
        MATERIALCOLORS.add("#03a9f4");
        MATERIALCOLORS.add("#039be5");
        MATERIALCOLORS.add("#0288d1");
        MATERIALCOLORS.add("#0277bd");
        MATERIALCOLORS.add("#01579b");
        MATERIALCOLORS.add("#80d8ff");
        MATERIALCOLORS.add("#40c4ff");
        MATERIALCOLORS.add("#00b0ff");
        MATERIALCOLORS.add("#0091ea");

        /* Cyan */
        // MATERIALCOLORS.add("#00bcd4");
        MATERIALCOLORS.add("#e0f7fa");
        MATERIALCOLORS.add("#b2ebf2");
        MATERIALCOLORS.add("#80deea");
        MATERIALCOLORS.add("#4dd0e1");
        MATERIALCOLORS.add("#26c6da");
        MATERIALCOLORS.add("#00bcd4");
        MATERIALCOLORS.add("#00acc1");
        MATERIALCOLORS.add("#0097a7");
        MATERIALCOLORS.add("#00838f");
        MATERIALCOLORS.add("#006064");
        MATERIALCOLORS.add("#84ffff");
        MATERIALCOLORS.add("#18ffff");
        MATERIALCOLORS.add("#00e5ff");
        MATERIALCOLORS.add("#00b8d4");

        /* Teal */
        // MATERIALCOLORS.add("#009688");
        MATERIALCOLORS.add("#e0f2f1");
        MATERIALCOLORS.add("#b2dfdb");
        MATERIALCOLORS.add("#80cbc4");
        MATERIALCOLORS.add("#4db6ac");
        MATERIALCOLORS.add("#26a69a");
        MATERIALCOLORS.add("#009688");
        MATERIALCOLORS.add("#00897b");
        MATERIALCOLORS.add("#00796b");
        MATERIALCOLORS.add("#00695c");
        MATERIALCOLORS.add("#004d40");
        MATERIALCOLORS.add("#a7ffeb");
        MATERIALCOLORS.add("#64ffda");
        MATERIALCOLORS.add("#1de9b6");
        MATERIALCOLORS.add("#00bfa5");

        /* Green */
        // MATERIALCOLORS.add("#259b24");
        MATERIALCOLORS.add("#d0f8ce");
        MATERIALCOLORS.add("#a3e9a4");
        MATERIALCOLORS.add("#72d572");
        MATERIALCOLORS.add("#42bd41");
        MATERIALCOLORS.add("#2baf2b");
        MATERIALCOLORS.add("#259b24");
        MATERIALCOLORS.add("#0a8f08");
        MATERIALCOLORS.add("#0a7e07");
        MATERIALCOLORS.add("#056f00");
        MATERIALCOLORS.add("#0d5302");
        MATERIALCOLORS.add("#a2f78d");
        MATERIALCOLORS.add("#5af158");
        MATERIALCOLORS.add("#14e715");
        MATERIALCOLORS.add("#12c700");

        /* Light Green */
        // MATERIALCOLORS.add("#8bc34a");
        MATERIALCOLORS.add("#f1f8e9");
        MATERIALCOLORS.add("#dcedc8");
        MATERIALCOLORS.add("#c5e1a5");
        MATERIALCOLORS.add("#aed581");
        MATERIALCOLORS.add("#9ccc65");
        MATERIALCOLORS.add("#8bc34a");
        MATERIALCOLORS.add("#7cb342");
        MATERIALCOLORS.add("#689f38");
        MATERIALCOLORS.add("#558b2f");
        MATERIALCOLORS.add("#33691e");
        MATERIALCOLORS.add("#ccff90");
        MATERIALCOLORS.add("#b2ff59");
        MATERIALCOLORS.add("#76ff03");
        MATERIALCOLORS.add("#64dd17");

        /* Lime */
        // MATERIALCOLORS.add("#cddc39");
        MATERIALCOLORS.add("#f9fbe7");
        MATERIALCOLORS.add("#f0f4c3");
        MATERIALCOLORS.add("#e6ee9c");
        MATERIALCOLORS.add("#dce775");
        MATERIALCOLORS.add("#d4e157");
        MATERIALCOLORS.add("#cddc39");
        MATERIALCOLORS.add("#c0ca33");
        MATERIALCOLORS.add("#afb42b");
        MATERIALCOLORS.add("#9e9d24");
        MATERIALCOLORS.add("#827717");
        MATERIALCOLORS.add("#f4ff81");
        MATERIALCOLORS.add("#eeff41");
        MATERIALCOLORS.add("#c6ff00");
        MATERIALCOLORS.add("#aeea00");

        // Yellow
        // MATERIALCOLORS.add("#ffeb3b");
        MATERIALCOLORS.add("#fffde7");
        MATERIALCOLORS.add("#fff9c4");
        MATERIALCOLORS.add("#fff59d");
        MATERIALCOLORS.add("#fff176");
        MATERIALCOLORS.add("#ffee58");
        MATERIALCOLORS.add("#ffeb3b");
        MATERIALCOLORS.add("#fdd835");
        MATERIALCOLORS.add("#fbc02d");
        MATERIALCOLORS.add("#f9a825");
        MATERIALCOLORS.add("#f57f17");
        MATERIALCOLORS.add("#ffff8d");
        MATERIALCOLORS.add("#ffff00");
        MATERIALCOLORS.add("#ffea00");
        MATERIALCOLORS.add("#ffd600");

        /* Amber */
        // MATERIALCOLORS.add("#ffc107");
        MATERIALCOLORS.add("#fff8e1");
        MATERIALCOLORS.add("#ffecb3");
        MATERIALCOLORS.add("#ffe082");
        MATERIALCOLORS.add("#ffd54f");
        MATERIALCOLORS.add("#ffca28");
        MATERIALCOLORS.add("#ffc107");
        MATERIALCOLORS.add("#ffb300");
        MATERIALCOLORS.add("#ffa000");
        MATERIALCOLORS.add("#ff8f00");
        MATERIALCOLORS.add("#ff6f00");
        MATERIALCOLORS.add("#ffe57f");
        MATERIALCOLORS.add("#ffd740");
        MATERIALCOLORS.add("#ffc400");
        MATERIALCOLORS.add("#ffab00");

        /* Orange */
        // MATERIALCOLORS.add("#ff9800");
        MATERIALCOLORS.add("#fff3e0");
        MATERIALCOLORS.add("#ffe0b2");
        MATERIALCOLORS.add("#ffcc80");
        MATERIALCOLORS.add("#ffb74d");
        MATERIALCOLORS.add("#ffa726");
        MATERIALCOLORS.add("#ff9800");
        MATERIALCOLORS.add("#fb8c00");
        MATERIALCOLORS.add("#f57c00");
        MATERIALCOLORS.add("#ef6c00");
        MATERIALCOLORS.add("#e65100");
        MATERIALCOLORS.add("#ffd180");
        MATERIALCOLORS.add("#ffab40");
        MATERIALCOLORS.add("#ff9100");
        MATERIALCOLORS.add("#ff6d00");

        /* Deep Orange */
        // MATERIALCOLORS.add("#ff5722");
        MATERIALCOLORS.add("#fbe9e7");
        MATERIALCOLORS.add("#ffccbc");
        MATERIALCOLORS.add("#ffab91");
        MATERIALCOLORS.add("#ff8a65");
        MATERIALCOLORS.add("#ff7043");
        MATERIALCOLORS.add("#ff5722");
        MATERIALCOLORS.add("#f4511e");
        MATERIALCOLORS.add("#e64a19");
        MATERIALCOLORS.add("#d84315");
        MATERIALCOLORS.add("#bf360c");
        MATERIALCOLORS.add("#ff9e80");
        MATERIALCOLORS.add("#ff6e40");
        MATERIALCOLORS.add("#ff3d00");
        MATERIALCOLORS.add("#dd2c00");

        /* Brown */
        // MATERIALCOLORS.add("#795548");
        MATERIALCOLORS.add("#efebe9");
        MATERIALCOLORS.add("#d7ccc8");
        MATERIALCOLORS.add("#bcaaa4");
        MATERIALCOLORS.add("#a1887f");
        MATERIALCOLORS.add("#8d6e63");
        MATERIALCOLORS.add("#795548");
        MATERIALCOLORS.add("#6d4c41");
        MATERIALCOLORS.add("#5d4037");
        MATERIALCOLORS.add("#4e342e");
        MATERIALCOLORS.add("#3e2723");

        /* Grey */
        // MATERIALCOLORS.add("#9e9e9e");
        MATERIALCOLORS.add("#fafafa");
        MATERIALCOLORS.add("#f5f5f5");
        MATERIALCOLORS.add("#eeeeee");
        MATERIALCOLORS.add("#e0e0e0");
        MATERIALCOLORS.add("#bdbdbd");
        MATERIALCOLORS.add("#9e9e9e");
        MATERIALCOLORS.add("#757575");
        MATERIALCOLORS.add("#616161");
        MATERIALCOLORS.add("#424242");
        MATERIALCOLORS.add("#212121");
        MATERIALCOLORS.add("#000000");
        MATERIALCOLORS.add("#ffffff");

        /* Blue Grey */
        // MATERIALCOLORS.add("#607d8b");
        MATERIALCOLORS.add("#eceff1");
        MATERIALCOLORS.add("#cfd8dc");
        MATERIALCOLORS.add("#b0bec5");
        MATERIALCOLORS.add("#90a4ae");
        MATERIALCOLORS.add("#78909c");
        MATERIALCOLORS.add("#607d8b");
        MATERIALCOLORS.add("#546e7a");
        MATERIALCOLORS.add("#455a64");
        MATERIALCOLORS.add("#37474f");
        MATERIALCOLORS.add("#263238");
    }

    public static int randomColor() {
        Random randomGenerator = new Random();
        int randomIndex = randomGenerator.nextInt(MATERIALCOLORS.size());

        return Color.parseColor(MATERIALCOLORS.get(randomIndex));
    }

}
