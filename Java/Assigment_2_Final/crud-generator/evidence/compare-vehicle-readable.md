# Compare: WITH vs WITHOUT RAG

Description: A Vehicle with make, model, year, and price

## WITHOUT RAG (empty context)
```java
// ----- entityCode -----
@Entity
public class Vehicle {
    @Id
    private Long id;
    private String make;
    private String model;
    private int year;
    private double price;

    // getters and setters
}

// ----- repositoryCode -----
import org.springframework.data.jpa.repository.JpaRepository;

public interface VehicleRepository extends JpaRepository<Vehicle, Long> {
}

// ----- serviceCode -----
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class VehicleService {

    @Autowired
    private VehicleRepository vehicleRepository;

    public Iterable<Vehicle> getAllVehicles() {
        return vehicleRepository.findAll();
    }

    public Vehicle getVehicleById(Long id) {
        return vehicleRepository.findById(id).orElse(null);
    }

    public Vehicle createVehicle(Vehicle vehicle) {
        return vehicleRepository.save(vehicle);
    }

    public Vehicle updateVehicle(Long id, Vehicle updatedVehicle) {
        Vehicle existingVehicle = vehicleRepository.findById(id).orElse(null);
        if (existingVehicle != null) {
            existingVehicle.setMake(updatedVehicle.getMake());
            existingVehicle.setModel(updatedVehicle.getModel());
            existingVehicle.setYear(updatedVehicle.getYear());
            existingVehicle.setPrice(updatedVehicle.getPrice());
            return vehicleRepository.save(existingVehicle);
        }
        return null;
    }

    public void deleteVehicle(Long id) {
        vehicleRepository.deleteById(id);
    }
}

// ----- controllerCode -----
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/vehicles")
public class VehicleController {

    @Autowired
    private VehicleService vehicleService;

    @GetMapping
    public Iterable<Vehicle> getAllVehicles() {
        return vehicleService.getAllVehicles();
    }

    @GetMapping("/{id}")
    public Vehicle getVehicleById(@PathVariable Long id) {
        return vehicleService.getVehicleById(id);
    }

    @PostMapping
    public Vehicle createVehicle(@RequestBody Vehicle vehicle) {
        return vehicleService.createVehicle(vehicle);
    }

    @PutMapping("/{id}")
    public Vehicle updateVehicle(@PathVariable Long id, @RequestBody Vehicle updatedVehicle) {
        return vehicleService.updateVehicle(id, updatedVehicle);
    }

    @DeleteMapping("/{id}")
    public void deleteVehicle(@PathVariable Long id) {
        vehicleService.deleteVehicle(id);
    }
}
```

## WITH RAG (retrieved conventions)
```java
// ----- entityCode -----
@Entity
@Table(name = "vehicles")
public class Vehicle {
    @Id
    private Long id;

    private String make;
    private String model;
    private int year;
    private BigDecimal price;

    // getters and setters
}

// ----- repositoryCode -----
@Repository
public interface VehicleRepository extends JpaRepository<Vehicle, Long> {
}

// ----- serviceCode -----
@Service
@Transactional
public class VehicleService {

    private final VehicleRepository vehicleRepository;

    public VehicleService(VehicleRepository vehicleRepository) {
        this.vehicleRepository = vehicleRepository;
    }

    public Vehicle createVehicle(Vehicle vehicle) {
        return vehicleRepository.save(vehicle);
    }

    public Optional<Vehicle> findById(Long id) throws EntityNotFoundException {
        return vehicleRepository.findById(id).orElseThrow(EntityNotFoundException::new);
    }

    public List<Vehicle> findAll() {
        return (List<Vehicle>) vehicleRepository.findAll();
    }

    public Vehicle updateVehicle(Vehicle vehicle) {
        return vehicleRepository.save(vehicle);
    }

    public void deleteVehicle(Long id) throws EntityNotFoundException {
        findById(id);
        vehicleRepository.deleteById(id);
    }
}

// ----- controllerCode -----
@RestController
@RequestMapping("/api/vehicles")
public class VehicleController {

    private final VehicleService vehicleService;

    @Autowired
    public VehicleController(VehicleService vehicleService) {
        this.vehicleService = vehicleService;
    }

    @PostMapping
    public ResponseEntity<Vehicle> createVehicle(@RequestBody Vehicle vehicle) {
        Vehicle createdVehicle = vehicleService.createVehicle(vehicle);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdVehicle);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Vehicle> findById(@PathVariable Long id) throws EntityNotFoundException {
        Vehicle foundVehicle = vehicleService.findById(id);
        return ResponseEntity.ok(foundVehicle);
    }

    @GetMapping
    public ResponseEntity<List<Vehicle>> findAll() {
        List<Vehicle> vehicles = vehicleService.findAll();
        return ResponseEntity.ok(vehicles);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Vehicle> updateVehicle(@PathVariable Long id, @RequestBody Vehicle vehicleDetails) throws EntityNotFoundException {
        vehicleDetails.setId(id);
        Vehicle updatedVehicle = vehicleService.updateVehicle(vehicleDetails);
        return ResponseEntity.ok(updatedVehicle);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteVehicle(@PathVariable Long id) throws EntityNotFoundException {
        vehicleService.deleteVehicle(id);
        return ResponseEntity.noContent().build();
    }
}
```
