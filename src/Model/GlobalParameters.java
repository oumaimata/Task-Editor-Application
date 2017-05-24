package Model;

import java.util.Enumeration;

/**
 * Created by Théo on 17/05/2017.
 */
public class GlobalParameters {
    public enum TypePrecondition {
        NOMOLOGIQUE,
        REGLEMENTAIRE,
        CONTEXTUELLE,
        FAVORABLE
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

    public enum RelationAllen{
        // X is the left part of the assertion and Y the right part

        before("<"), // X takes place before Y
        after(">"), // X takes place after Y
        meet("m"), // X meets Y, X est directement suivi par Y
        meetInverse("mi"), // Y meets X, Y est directement suivi par X
        overlap("o"), // X overlaps with Y
        overlapInverse("oi"), // Y overlaps with X
        start("s"), // X start at the same time as Y
        startInverse("si"), // Y start at the same time as X
        during("d"), // X happens during Y
        duringInverse("di"), // Y happens during X
        finish("f"), // X finishes at the same time as Y
        finishInverse("fi"), // Y finishes at the same time as X
        equal("="); // X happens at the same time as Y

        private String symbol;

        RelationAllen(String symbol) {
            this.symbol = symbol;
        }

        public String getSymbol() {
            return symbol;
        }
    }

    public enum TypeConstructeur{
        IND,
        SEQ,
        SEQ_ORD,
        PAR,
        PAR_SIM,
        PAR_START,
        PAR_END
    }
}
