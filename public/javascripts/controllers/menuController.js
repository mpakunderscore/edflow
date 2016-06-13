edflow

    .controller("menuController", function ($scope, $rootScope, $state) {

        $scope.version = "0.4";

        $rootScope.language = localStorage.getItem("language");

        if ($rootScope.language === null)
            $rootScope.language = "EN";

        $scope.menuItems = [];

        var menuItem  = {title: "MENU"};

        // data-ui-sref="{{item.url}}"

        var settingsItem = {title: "SETTINGS", class: "ng-hide"};
        var languageItem = {title: "LANGUAGE", class: "ng-hide"};

        var EN = {title: "ENGLISH", class: "choice ng-hide", code: "EN"};
        //var DE = {title: "GERMAN",  class: "choice ng-hide", code: "DE"};
        //var ES = {title: "SPANISH", class: "choice ng-hide", code: "ES"};
        var RU = {title: "RUSSIAN", class: "choice ng-hide", code: "RU"};
        //var ZH = {title: "CHINESE", class: "choice ng-hide", code: "ZH"};

        $scope.menuItems = [RU, EN, languageItem, menuItem]; //settingsItem


        $scope.selectLanguage = function (item) {

            $rootScope.language = item.code;
            localStorage.setItem("language", item.code);

            //$state.transitionTo("app");
        }

        $scope.menuEnter = function () {

            menuItem.class = "selected";
            settingsItem.class = "";
            languageItem.class = "";
        }

        $scope.menuLeave = function () {

            menuItem.class = "";
            settingsItem.class = "ng-hide";
            languageItem.class = "ng-hide";
            hideLanguages();
        }

        function showLanguages() {
            EN.class = "choice"
            RU.class = "choice";
        }

        function hideLanguages() {
            EN.class = "choice ng-hide"
            RU.class = "choice ng-hide";
        }

        $scope.menu = function (item) {

            if (item.title === "LANGUAGE") {

                if (item.class === "") {
                    settingsItem.class = "ng-hide";
                    languageItem.class = "selected";
                    showLanguages();
                } else {
                    settingsItem.class = "";
                    languageItem.class = "";
                    hideLanguages();
                }
            }

            if (item.class === "choice") {
                $scope.selectLanguage(item);
            }
        }
    })


