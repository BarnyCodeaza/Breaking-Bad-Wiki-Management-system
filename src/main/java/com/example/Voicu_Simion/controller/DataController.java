package com.example.Voicu_Simion.controller;

import com.example.Voicu_Simion.Service.DataService;
import com.example.Voicu_Simion.entity.DataEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/data")
@CrossOrigin(origins = "*")
public class DataController {

    @Autowired
    private DataService dataService;

    // 1. Endpoint pentru detalii (Un singur personaj)
    // Se apelează: /api/data?id=50
    @GetMapping("")
    public ResponseEntity<DataEntity> getData(@RequestParam Integer id) {
        return dataService.getData(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // 2. Endpoint pentru LISTĂ (Căutare + Status + Sezon + Paginare)
    // Se apelează: /api/data/all?page=1&size=10&q=Walter&status=Alive&season=2
    @GetMapping("/all")
    public ResponseEntity<Map<String, Object>> getAllData(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String q,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) Integer season
    ) {
        // Aici trimitem TOȚI parametrii către Service (inclusiv sezonul)
        return ResponseEntity.ok(dataService.getAllFiltered(q, status, season, page, size));
    }

    // 3. Endpoint pentru SALVARE (Adăugare sau Editare) - Folosit de Admin
    @PostMapping("/save")
    public ResponseEntity<DataEntity> saveData(@RequestBody DataEntity entity) {
        DataEntity savedEntity = dataService.saveData(entity);
        return ResponseEntity.ok(savedEntity);
    }

    // 4. Endpoint pentru ȘTERGERE - Folosit de Admin
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteData(@PathVariable Integer id) {
        dataService.deleteData(id);
        return ResponseEntity.ok().build();
    }
}