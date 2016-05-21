edflow

    .config(function ($stateProvider, $urlRouterProvider, $locationProvider) {

        $urlRouterProvider.otherwise("");

        $stateProvider

            .state("app", {
                url: "",
                //templateUrl: "assets/view/data/content.html",
                //controller: "edflowController as edflow"
            })

            .state("settings", {
                url: "/settings",
                templateUrl: "assets/view/data/content.html",
                controller: "settingsController as settings"
            })

            .state("news", {
                url: "/news",
                templateUrl: "assets/view/data/content.html",
                controller: "newsController as news"
            })

            .state("words", {
                url: "/words",
                templateUrl: "assets/view/data/words.html",
                controller: "wordsController as words"
            })

            .state("translate", {
                url: "/translate",
                templateUrl: "assets/view/data/translate.html",
                controller: "translateController as content"
            })

            .state("wikipedia", {
                url: "/wikipedia",
                templateUrl: "assets/view/data/content.html",
                controller: "wikipediaController as wikipedia"
            })

            .state("book", {
                url: "/book",
                templateUrl: "assets/view/data/content.html",
                controller: "bookController as book"
            })

            .state("flow", {
                url: "/flow",
                templateUrl: "assets/view/data/flow.html",
                controller: "flowController as flow"
            })

            .state("arxiv", {
                url: "/arxiv",
                templateUrl: "assets/view/data/flow.html",
                controller: "arxivController as arxiv"
            })

        //$locationProvider.html5Mode(true);
    });
