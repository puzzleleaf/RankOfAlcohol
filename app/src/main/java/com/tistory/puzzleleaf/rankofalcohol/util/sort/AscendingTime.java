package com.tistory.puzzleleaf.rankofalcohol.util.sort;

import com.tistory.puzzleleaf.rankofalcohol.model.BattleObject;

import java.util.Comparator;

/**
 * Created by cmtyx on 2017-08-24.
 */

public class AscendingTime implements Comparator<BattleObject> {

    @Override
    public int compare(BattleObject o1, BattleObject o2) {
        return o1.getOutTime() > o2.getOutTime() ? 1 : o1.getOutTime() == o2.getOutTime() ? 0 : -1;
    }
}
