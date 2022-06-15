const validation = (values) => {

    let errors = {
        noSender: true
    };

    if (!values.name) {
        errors.name = "Поле имя некорректно";
    }else if(values.name.length < 2){
        errors.name = "Имя должно состоять из минимум 2 букв"
    }

    if (!values.username) {
        errors.username = "Поле псевдонима некорректно";
    }else if(values.username.length < 4){
        errors.username = "Имя должно состоять из минимум 4 символов"
    }

    if (!values.email) {
        errors.email = "Поле почты обязательно";
    } else if (!/\S+@\S+\.\S+/.test(values.email)) {
        errors.email = "Поле почты неккоректно";
    }

    if(!values.password){
        errors.password="Поле пароля обязательно";
    } else if(values.password.length < 4){
        errors.password = "Пароль должен состоять из минимум 4 символов"
    }

    //console.log(errors);
    return errors;
};

export default validation;