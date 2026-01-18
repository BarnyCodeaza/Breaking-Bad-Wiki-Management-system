package com.example.Voicu_Simion.Service;

import com.example.Voicu_Simion.entity.DataEntity;
import com.example.Voicu_Simion.repository.DataRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class DataService {

    @Autowired
    private DataRepository dataRepository;

    // Metoda veche (pentru detalii)
    public Optional<DataEntity> getData(Integer id){
        return dataRepository.findById(id);
    }

    // --- METODA ACTUALIZATĂ CU SEZON ---
    public Map<String, Object> getAllFiltered(String query, String status, Integer season, int page, int size) {
        List<DataEntity> allItems = dataRepository.findAll();

        List<DataEntity> filteredItems = allItems.stream()
                .filter(item -> {
                    Map<String, Object> data = item.getData();
                    if (data == null) return false;

                    String name = (String) data.getOrDefault("name", "");
                    String nickname = (String) data.getOrDefault("nickname", "");
                    String itemStatus = (String) data.getOrDefault("status", "");

                    // Citim lista de sezoane din JSON (poate fi null)
                    List<Integer> appearances = (List<Integer>) data.get("appearance");

                    // 1. Filtru Nume
                    boolean matchesName = (query == null || query.isEmpty()) ||
                            name.toLowerCase().contains(query.toLowerCase()) ||
                            nickname.toLowerCase().contains(query.toLowerCase());

                    // 2. Filtru Status
                    boolean matchesStatus = (status == null || status.isEmpty() || status.equals("All")) ||
                            itemStatus.equalsIgnoreCase(status);

                    // 3. Filtru Sezon (NOU!)
                    // Dacă season e 0 sau null, ignorăm filtrul (le arătăm pe toate)
                    // Altfel, verificăm dacă lista 'appearances' conține sezonul ales
                    boolean matchesSeason = (season == null || season == 0) ||
                            (appearances != null && appearances.contains(season));

                    return matchesName && matchesStatus && matchesSeason;
                })
                .collect(Collectors.toList());

        // --- De aici jos e la fel ca înainte (Paginarea) ---
        int totalItems = filteredItems.size();
        int totalPages = (int) Math.ceil((double) totalItems / size);

        if (page < 1) page = 1;
        if (page > totalPages && totalPages > 0) page = totalPages;

        int start = (page - 1) * size;
        int end = Math.min(start + size, totalItems);

        List<DataEntity> pageContent = new ArrayList<>();
        if (start < totalItems && start >= 0) {
            pageContent = filteredItems.subList(start, end);
        }

        Map<String, Object> response = new HashMap<>();
        response.put("content", pageContent);
        response.put("currentPage", page);
        response.put("totalItems", totalItems);
        response.put("totalPages", totalPages);

        return response;
    }
    // Metoda pentru salvare (Adăugare sau Editare)
    public DataEntity saveData(DataEntity entity) {
        return dataRepository.save(entity);
    }

    // Metoda pentru ștergere
    public void deleteData(Integer id) {
        dataRepository.deleteById(id);
    }
}