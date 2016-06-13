edflow

    .config(function ($stateProvider, $urlRouterProvider, $locationProvider) {

        $urlRouterProvider.otherwise("");

        $stateProvider

            .state("app", {
                url: "",
                //templateUrl: "assets/view/data/content.html",
                //controller: "edflowController as edflow"
            })

            // .state("settings", {
            //     url: "/settings",
            //     templateUrl: "assets/view/data/content.html",
            //     controller: "settingsController as settings"
            // })

            .state("Main", {
                url: "/main",
                templateUrl: "assets/view/data/content.html",
                controller: "contentController as content"
            })

            .state("Recommend", {
                url: "/recommend",
                templateUrl: "assets/view/data/content.html",
                controller: "contentController as content"
            })

            .state("Mine", {
                url: "/mine",
                templateUrl: "assets/view/data/content.html",
                controller: "contentController as content"
            })



        //$locationProvider.html5Mode(true);
    });
