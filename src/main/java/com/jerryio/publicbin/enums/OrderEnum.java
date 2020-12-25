package com.jerryio.publicbin.enums;

import org.bukkit.Material;

import com.jerryio.publicbin.interfaces.BinItemComparison;

@SuppressWarnings("deprecation")
public enum OrderEnum {
    Type((a, b) -> {
        Material aVal = a.item.getType();
        Material bVal = b.item.getType();
        
        if (aVal.equals(bVal)) {
            return 0;
        } else {
            String[] aNameArray = aVal.toString().split("_");
            String[] bNameArray = bVal.toString().split("_");
            
            return compare(aNameArray, bNameArray);
        }
    }),
    Metadata((a, b) -> {
        int first = compare(a.item.hasItemMeta(), b.item.hasItemMeta());
        if (first != 0)
            return first;
        else
            return compare(
                    a.item.getItemMeta().getEnchants().keySet().size(),
                    b.item.getItemMeta().getEnchants().keySet().size());

    }), 
    Durability((a, b) -> compare(a.item.getDurability(), b.item.getDurability())),
    Amount((a, b) -> compare(a.item.getAmount(), b.item.getAmount())),
    Time((a, b) -> compare(a.placedTime, b.placedTime));

    private BinItemComparison comparision;

    private OrderEnum(BinItemComparison com) {
        comparision = com;
    }

    public BinItemComparison getComparision() {
        return comparision;
    }
    
    private static int compare(String[] aVal, String[] bVal) {
        int maxLength = Math.max(aVal.length, bVal.length);
        
        for (int i = 1; i <= maxLength; i++) {
            int aIdx = aVal.length - i;
            int bIdx = bVal.length - i;
            if (aIdx < 0) return -1;
            if (bIdx < 0) return 1;
            
            String a = aVal[aIdx];
            String b = bVal[bIdx];
            int ans = compare(a, b);
            if (ans != 0) return ans;
        }
        
        return 0;
    }

    private static int compare(String aVal, String bVal) {
        int ans = aVal.toString().compareTo(bVal.toString());
        if (ans > 0)
            return 1;
        else if (ans < 0)
            return -1;
        else
            return 0;
    }
    
    private static int compare(long aVal, long bVal) {
        if (aVal > bVal)
            return 1;
        else if (aVal < bVal)
            return -1;
        else
            return 0;
    }

    private static int compare(boolean aHas, boolean bHas) {
        if (aHas && bHas)
            return 0;
        else if (aHas)
            return 1;
        else if (bHas)
            return -1;
        else
            return 0;
    }
}
