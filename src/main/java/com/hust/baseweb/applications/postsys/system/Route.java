package com.hust.baseweb.applications.postsys.system;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Route {
    private boolean isSolutionFound;
    private List<List<Integer>> indexes;
    private List<Long> distance;
}
