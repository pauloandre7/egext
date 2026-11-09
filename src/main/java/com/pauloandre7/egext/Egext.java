package com.pauloandre7.egext;

import com.pauloandre7.egext.presenters.EventoPresenter;
import com.pauloandre7.egext.presenters.InscritoPresenter;
import com.pauloandre7.egext.presenters.VoluntarioPresenter;
import com.pauloandre7.egext.repositories.text.EventoRepositoryText;
import com.pauloandre7.egext.repositories.IEventoRepository;
import com.pauloandre7.egext.repositories.IInscritoRepository;
import com.pauloandre7.egext.repositories.IVoluntarioRepository;
import com.pauloandre7.egext.repositories.binary.EventoRepositoryBinary;
import com.pauloandre7.egext.repositories.binary.InscritoRepositoryBinary;
import com.pauloandre7.egext.repositories.binary.VoluntarioRepositoryBinary;
import com.pauloandre7.egext.views.InscritosView;
import com.pauloandre7.egext.views.swingui.EventosView;
import com.pauloandre7.egext.views.swingui.VoluntariosView;

public class Egext {

    public static void main(String[] args) {
        try {
            String path = "./";
            
            // Inicialização dos repositórios nas interfaces
            IInscritoRepository inscritoRepo = new InscritoRepositoryBinary(path);
            IVoluntarioRepository voluntarioRepo = new VoluntarioRepositoryBinary(path);
            IEventoRepository eventoRepo = new EventoRepositoryBinary(path);

            // Injeção das dependências
            VoluntariosView volView = new VoluntariosView();
            VoluntarioPresenter volPresenter = new VoluntarioPresenter(volView, voluntarioRepo);

            InscritosView insView = new InscritosView();
            InscritoPresenter insPresenter = new InscritoPresenter(insView, inscritoRepo);

            EventosView telaEventos = new EventosView();

            EventoPresenter eventoPresenter = new EventoPresenter(
                telaEventos, 
                eventoRepo, 
                inscritoRepo, 
                voluntarioRepo,
                volPresenter,
                volView,
                insPresenter,
                insView
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
