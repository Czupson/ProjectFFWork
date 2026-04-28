package repo;

import domain.resource.Resource;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class InMemoryResourceRepository implements ResourceRepository {
    private final List<Resource> resources = new ArrayList<>();

    @Override
    public void add(Resource resource) {
        if (resource == null) {
            throw new IllegalArgumentException("Resource cannot be null");
        }
        resources.add(resource);
    }

    @Override
    public Optional<Resource> findByName(String name) {
        return resources.stream().filter(r -> r.getName().equals(name)).findFirst();
    }

    @Override
    public List<Resource> findAll() {
        return new ArrayList<>(resources);
    }
}
