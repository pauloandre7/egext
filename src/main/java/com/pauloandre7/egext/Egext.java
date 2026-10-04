package com.pauloandre7.egext;

import com.pauloandre7.egext.presenters.EventoPresenter;
import com.pauloandre7.egext.presenters.InscritoPresenter;
import com.pauloandre7.egext.presenters.VoluntarioPresenter;
import com.pauloandre7.egext.repositories.EventoRepositoryText;
import com.pauloandre7.egext.repositories.IEventoRepository;
import com.pauloandre7.egext.repositories.IInscritoRepository;
import com.pauloandre7.egext.repositories.IVoluntarioRepository;
import com.pauloandre7.egext.repositories.InscritoRepositoryText;
import com.pauloandre7.egext.repositories.VoluntarioRepositoryText;
import com.pauloandre7.egext.views.swingui.EventosView;
import com.pauloandre7.egext.views.MockInscritoView;
import com.pauloandre7.egext.views.MockVoluntariosView;

public class Egext {

    public static void main(String[] args) {
        try {
            String path = "./";
            
            IInscritoRepository inscritoRepo = new InscritoRepositoryText(path);
            IVoluntarioRepository voluntarioRepo = new VoluntarioRepositoryText(path);
            IEventoRepository eventoRepo = new EventoRepositoryText(path, inscritoRepo, voluntarioRepo);

            MockVoluntariosView volView = new MockVoluntariosView();
            VoluntarioPresenter volPresenter = new VoluntarioPresenter(volView, voluntarioRepo);

            MockInscritoView insView = new MockInscritoView();
            InscritoPresenter insPresenter = new InscritoPresenter(insView, inscritoRepo);

            System.out.println("\n>>> Cadastrando Responsável e Participantes de teste...");
            volView.setDadosFormulario(0, "RA123456", "Paulo André", "paulo@email.com", "(11) 98765-4321");
            volPresenter.registrarVoluntario();

            volView.setDadosFormulario(0, "RA654321", "Maria Silva", "maria@email.com", "(11) 91234-5678");
            volPresenter.registrarVoluntario();

            insView.setDadosFormulario("Carlos Oliveira", "carlos@email.com", "(11) 95555-4444");
            insPresenter.registrarInscrito();

            EventosView telaEventos = new EventosView();

            EventoPresenter eventoPresenter = new EventoPresenter(
                telaEventos, 
                eventoRepo, 
                inscritoRepo, 
                voluntarioRepo
            );

            telaEventos.setPresenter(eventoPresenter);

            telaEventos.setLocationRelativeTo(null);
            telaEventos.setVisible(true);

            telaEventos.listarEventos(eventoRepo.findAll());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
