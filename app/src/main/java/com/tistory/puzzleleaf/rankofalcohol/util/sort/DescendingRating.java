package com.tistory.puzzleleaf.rankofalcohol.util.sort;

import com.tistory.puzzleleaf.rankofalcohol.model.RankObject;

import java.util.Comparator;

/**
 * Created by cmtyx on 2017-08-14.
 */

//별점순 정렬
public class DescendingRating implements Comparator<RankObject> {
    @Override
    public int compare(RankObject o1, RankObject o2) {
        return o1.getScore() < o2.getScore() ? 1 : o1.getScore() == o2.getScore() ? 0 : -1;
    }
}