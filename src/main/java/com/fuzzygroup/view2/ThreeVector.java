package com.fuzzygroup.view2;

public class ThreeVector {

    private double x;
    private double y;
    private double z;

    public ThreeVector(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public ThreeVector plus(ThreeVector term) {
        return new ThreeVector(this.x + term.getX(),
                this.y + term.getY(),
                this.z + term.getZ());
    }

    public ThreeVector minus(ThreeVector subtrahend) {
        return new ThreeVector(x - subtrahend.x, y - subtrahend.y, z - subtrahend.z);
    }

    public ThreeVector coordinatewiseMultiplication(ThreeVector factor) {
        return new ThreeVector(this.x * factor.getX(),
                this.y * factor.getY(),
                this.z * factor.getZ());
    }

    public ThreeVector multiple(double factor) {
        return new ThreeVector(x * factor, y * factor, z * factor);
    }

    public ThreeVector divide(double number) {
        return new ThreeVector(x / number, y / number, z / number);
    }

    public ThreeVector divide(ThreeVector divider) {
        return new ThreeVector(x / divider.x, y / divider.y, z / divider.z);
    }

    public double module() {
        return Math.abs(Math.pow(x * x + y * y + z * z, 0.5));
    }

    public ThreeVector clone() {
        return new ThreeVector(x, y, z);
    }


    public ThreeVector normalize() {
        return new ThreeVector(x / this.module(), y / this.module(), z / this.module());
    }

    public double angle() {
        double angle = 0;
        ThreeVector norma = this.normalize();
        double innerAngle = Math.atan(Math.abs(norma.y / norma.x));
        if (x >= 0 && y >= 0) {
            angle = innerAngle;
        } else if (x >= 0 && y < 0) {
            angle = 2 * Math.PI - innerAngle;
        } else if (x < 0 && y >= 0) {
            angle = Math.PI / 2 - innerAngle + Math.PI / 2;
        } else if (x < 0 && y < 0) {
            angle = innerAngle + Math.PI;
        } else {
            System.err.println("Wrong angle value: " + innerAngle);
        }
        return angle;
    }

    public ThreeVector projectOn(ThreeVector vector) {
        double angle = this.angle();
        double angle2 = vector.angle();
        double deltaAngle = angle - angle2;
        if (Math.abs(deltaAngle) <= Math.PI / 2) {
            return new PolarCoordinateSystemVector(vector.angle(), Math.abs(Math.cos(deltaAngle) * this.module()))
                    .toFlatCartesianVector();
        } else {
            return new PolarCoordinateSystemVector(vector.angle() + Math.PI, Math.abs(Math.cos(deltaAngle) * this.module()))
                    .toFlatCartesianVector();
        }
    }

    public ThreeVector projectRestOn(ThreeVector vector) {
        double angle = this.angle();
        double angle2 = vector.angle();
        double deltaAngle = (angle - angle2) % (2 * Math.PI);
        double resultAngle = vector.angle() + Math.PI / 2;
        if (deltaAngle > Math.PI || (deltaAngle < 0 && deltaAngle > -Math.PI)) {
            resultAngle *= -1;
        }
        return new PolarCoordinateSystemVector(resultAngle, Math.abs(Math.sin(deltaAngle) * this.module()))
                .toFlatCartesianVector();
    }

    public PolarCoordinateSystemVector toPolar() {
        double r;
        double phi;
        r = Math.sqrt(x * x + y * y);
        if (x == 0) {
            if (y == 0) {
                phi = 0;
            } else if (y > 0) {
                phi = Math.PI / 2;
            } else {
                phi = 3 * Math.PI / 2;
            }
        } else if (x > 0) {
            if (y >= 0) {
                phi = Math.atan(y / x);
            } else {
                phi = Math.atan(y / x) + 2 * Math.PI;
            }
        } else {
            phi = Math.atan(y / x) + Math.PI;
        }
        return new PolarCoordinateSystemVector(phi, r);
    }

    public class PolarCoordinateSystemVector {
        private double radian;
        private double module;

        public PolarCoordinateSystemVector(double radian, double module) {
            this.radian = radian;
            this.module = module;
        }

        public ThreeVector toFlatCartesianVector() {
            return new ThreeVector(Math.cos(radian) * module,
                    Math.sin(radian) * module,
                    0);
        }

        public double getRadian() {
            return radian;
        }

        public void setRadian(double radian) {
            this.radian = radian;
        }

        public double getModule() {
            return module;
        }

        public void setModule(double module) {
            this.module = module;
        }
    }

    @Override
    public String toString() {
        return toStringPrecisely(3);
    }

    public String toStringPrecisely(int amountNumbersAfterComma) {
        return "vector(" + MathUtils.round(x, amountNumbersAfterComma)
                + "; " + MathUtils.round(y, amountNumbersAfterComma)
                + "; " + MathUtils.round(z, amountNumbersAfterComma) + ")";
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getZ() {
        return z;
    }
}
