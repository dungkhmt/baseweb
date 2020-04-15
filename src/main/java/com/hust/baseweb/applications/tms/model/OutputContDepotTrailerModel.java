package com.hust.baseweb.applications.tms.model;

import com.hust.baseweb.applications.tms.entity.ContDepotTrailer;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class OutputContDepotTrailerModel {
    List<ContDepotTrailer> list;
}
