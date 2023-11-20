package org.edelweiss.logging.properties;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * @author fzw
 * @date 2023/11/20
 **/
@Data
@NoArgsConstructor
public class LogTagProperties {

    private List<String> global = new ArrayList<>();
}
