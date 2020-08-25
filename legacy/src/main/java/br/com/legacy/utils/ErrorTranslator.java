package br.com.legacy.utils;

import android.content.Context;


import br.com.legacy.managers.dtos.ErrorResponse;
import br.com.R;

/**
 * Created by ruderos on 8/18/17.
 */

public class ErrorTranslator {

    public static int[] sucessCodes = {200, 201, 0};

    public static ErrorResponse translateError(int status) {

        for (int i:sucessCodes) {
            if (i == status) return null;
        }

        ErrorResponse error = new ErrorResponse();

        String message = "Erro não identificado";

        if (status == 0) {//Falha de comunicação
            message = "Falha de comunicação do servidor\n";
        }

        if (status == 400) {//Bad request
            message = "Campos faltando\n";
        }

        if (status == 401) {//Unauthorized
          message = "Sem premissão";
        }

        if (status == 403) {//Forbidden
            message = "Sem premissão";
        }

        if (status == 404) {//Not Found
            message = "Recurso não encontrado\n";
        }

        if (status == 409) {//Not Found
            message = "Campos já existente\n";
        }

        if (status == 412) {//Pre-Condition failed
            message = "Inconsistência no request nos campos\n";
        }

        if (status == 500) {//Server Error
            message = "Erro no servidor";
        }

        if (status == 666) {//noConnection
            message = "Você não possui conexão com a internet";//A gnt ta usando snackBar pra mostrar esse erro, então nao precisaria da mensagem
        }

        error.message = message;
        error.statusCode = status;

        return error;
    }


}
