package com.arch;

import java.util.Map;
import java.util.Set;

import io.quarkus.test.junit.QuarkusTestProfile;

public class RunWithStaging implements QuarkusTestProfile {

    @Override
    public Map<String, String> getConfigOverrides() {
        return Map.of("quarkus.profile", "staging");
    }

    @Override
    public Set<String> tags() {
        return Set.of("staging");
    }

}
