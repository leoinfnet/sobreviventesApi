package br.com.infnet.sobreviventesapi.repository;

import br.com.infnet.sobreviventesapi.api.dto.report.ContagemInfectadosReport;
import br.com.infnet.sobreviventesapi.api.dto.report.InfectadoEmZonaSeguraReport;
import br.com.infnet.sobreviventesapi.api.dto.report.SobreviventesPorComunidadeReport;
import br.com.infnet.sobreviventesapi.api.dto.report.TopSobreviventeRecursosReport;
import br.com.infnet.sobreviventesapi.domain.Sobrevivente;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ReportRepository extends JpaRepository<Sobrevivente, Long> {
    @Query("""
    select new br.com.infnet.sobreviventesapi.api.dto.report.ContagemInfectadosReport(
        sum(case when s.infectado = true then 1 else 0 end),
        sum(case when s.infectado = false then 1 else 0 end)
    )
    from Sobrevivente s
""")
    ContagemInfectadosReport reportInfectados();

    @Query("""
    select new br.com.infnet.sobreviventesapi.api.dto.report.SobreviventesPorComunidadeReport(
        c.id, c.nome, count(s.id)
    )
    from Comunidade c
    left join c.membros s
    group by c.id, c.nome
    order by count(s.id) desc
""")
    List<SobreviventesPorComunidadeReport> sobreviventesPorComunidade();

    @Query("""
    select new br.com.infnet.sobreviventesapi.api.dto.report.TopSobreviventeRecursosReport(
        s.id, s.nome, count(r.id)
    )
    from Sobrevivente s
    left join s.recursos r
    group by s.id, s.nome
    order by count(r.id) desc, s.id asc
""")
    List<TopSobreviventeRecursosReport> topSobreviventesPorRecursos(Pageable pageable);


    @Query("""
    select new br.com.infnet.sobreviventesapi.api.dto.report.InfectadoEmZonaSeguraReport(
        s.id, s.nome, c.nome
    )
    from Sobrevivente s
    join s.comunidades c
    where s.infectado = true
      and c.zonaSegura = true
    order by c.nome asc, s.nome asc
""")
    List<InfectadoEmZonaSeguraReport> infectadosEmZonasSeguras();


    // Exemplo extra para slide: "scroll" de infectados (Slice) sem count(*)
    @Query("""
        select s
        from Sobrevivente s
        where s.infectado = true
        order by s.id asc
    """)
    Slice<Sobrevivente> sliceInfectados(Pageable pageable);
}
