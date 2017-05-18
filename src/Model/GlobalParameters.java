package Model;

/**
 * Created by Théo on 17/05/2017.
 */
public class GlobalParameters {
    public enum TypePrecondition {
        nomologique,
        reglementaire,
        contextuelle,
        favorable
    }

    public enum TypeConditionArret {
        satisfaction,
        assertion,
        duree,
        iteration,
        instance
    }

    public enum TypeCondition {
        precondition,
        satisfaction,
        arret
    }

    public enum TypeRelationMother {
        IND,
        SEQ,
        SEQ_ORD,
        PAR,
        PAR_SIM,
        PAR_START,
        PAR_END
    }

    public enum TypeAssertion {
        activity,
        world
    }

    public enum OperateurLogique {
        AND,
        OR,
        XOR,
        NOT
    }

    public enum Nature {
        interruptible,
        optionnelle,
        iterative
    }
}
